package com.mentora.learning;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mentora.common.TenantContext;
import com.mentora.user.UserData;
import com.mentora.user.UserDataDAO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LearningServiceImpl implements LearningService {

	private final CourseRepository courseRepository;
	private final CourseModuleRepository moduleRepository;
	private final LessonRepository lessonRepository;
	private final EnrollmentRepository enrollmentRepository;
	private final LessonProgressRepository progressRepository;
	private final UserDataDAO userDataDAO;
	private final TenantContext tenantContext;

	@Override
	public Course createCourse(Course course, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		course.setTenantId(tenantId);
		course.setActive(true);
		return courseRepository.save(course);
	}

	@Override
	public List<Course> courses(HttpServletRequest httpServletRequest) {
		return courseRepository.findByTenantId(tenantContext.requireTenantId(httpServletRequest));
	}

	@Override
	public CourseModule createModule(UUID courseId, CourseModule module, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Course course = tenantCourse(tenantId, courseId);
		module.setTenantId(tenantId);
		module.setCourse(course);
		return moduleRepository.save(module);
	}

	@Override
	public List<CourseModule> modules(UUID courseId, HttpServletRequest httpServletRequest) {
		return moduleRepository.findByTenantIdAndCourseId(tenantContext.requireTenantId(httpServletRequest), courseId);
	}

	@Override
	public Lesson createLesson(UUID moduleId, Lesson lesson, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		CourseModule module = moduleRepository.findById(moduleId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("Module not found for tenant"));
		lesson.setTenantId(tenantId);
		lesson.setModule(module);
		return lessonRepository.save(lesson);
	}

	@Override
	public List<Lesson> lessons(UUID moduleId, HttpServletRequest httpServletRequest) {
		return lessonRepository.findByTenantIdAndModuleId(tenantContext.requireTenantId(httpServletRequest), moduleId);
	}

	@Override
	public Enrollment enroll(UUID courseId, UUID userId, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Course course = tenantCourse(tenantId, courseId);
		UserData user = userDataDAO.findById(userId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("User not found for tenant"));
		Enrollment existingEnrollment = enrollmentRepository.findByTenantIdAndCourseIdAndUserId(tenantId, courseId, userId)
				.orElse(null);
		if (existingEnrollment != null) {
			return existingEnrollment;
		}
		validatePrerequisite(tenantId, course, userId);

		Enrollment enrollment = new Enrollment();
		enrollment.setTenantId(tenantId);
		enrollment.setCourse(course);
		enrollment.setUser(user);
		enrollment.setEnrolledAt(LocalDateTime.now());
		return enrollmentRepository.save(enrollment);
	}

	@Override
	@Transactional
	public LessonProgress progress(UUID lessonId, UUID userId, ProgressRequest requestPayload, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Lesson lesson = lessonRepository.findById(lessonId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("Lesson not found for tenant"));
		UUID courseId = lesson.getModule().getCourse().getId();
		Enrollment enrollment = requireEnrollment(tenantId, courseId, userId);
		UserData user = userDataDAO.findById(userId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("User not found for tenant"));
		LessonProgress progress = progressRepository.findByTenantIdAndLessonIdAndUserId(tenantId, lessonId, userId)
				.orElseGet(LessonProgress::new);
		progress.setTenantId(tenantId);
		progress.setLesson(lesson);
		progress.setUser(user);
		progress.setCompleted(requestPayload.completed());
		progress.setResumePositionSeconds(requestPayload.resumePositionSeconds());
		progress.setUpdatedAt(LocalDateTime.now());
		progress = progressRepository.save(progress);
		updateEnrollmentCompletion(tenantId, courseId, userId, enrollment);
		return progress;
	}

	private Course tenantCourse(UUID tenantId, UUID courseId) {
		return courseRepository.findById(courseId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("Course not found for tenant"));
	}

	private void validatePrerequisite(UUID tenantId, Course course, UUID userId) {
		UUID prerequisiteCourseId = course.getPrerequisiteCourseId();
		if (prerequisiteCourseId == null) {
			return;
		}
		if (prerequisiteCourseId.equals(course.getId())) {
			throw new IllegalArgumentException("Course cannot require itself as a prerequisite");
		}
		if (!enrollmentRepository.existsByTenantIdAndCourseIdAndUserIdAndCompletedAtIsNotNull(tenantId, prerequisiteCourseId, userId)) {
			throw new IllegalArgumentException("Prerequisite course must be completed before enrollment");
		}
	}

	private Enrollment requireEnrollment(UUID tenantId, UUID courseId, UUID userId) {
		return enrollmentRepository.findByTenantIdAndCourseIdAndUserId(tenantId, courseId, userId)
				.orElseThrow(() -> new IllegalArgumentException("User is not enrolled in this course"));
	}

	private void updateEnrollmentCompletion(UUID tenantId, UUID courseId, UUID userId, Enrollment enrollment) {
		long totalLessons = lessonRepository.countByTenantIdAndModuleCourseId(tenantId, courseId);
		long completedLessons = progressRepository.countCompletedLessonsForCourse(tenantId, courseId, userId);
		if (totalLessons > 0 && completedLessons >= totalLessons) {
			if (enrollment.getCompletedAt() == null) {
				enrollment.setCompletedAt(LocalDateTime.now());
				enrollmentRepository.save(enrollment);
			}
			return;
		}
		if (enrollment.getCompletedAt() != null) {
			enrollment.setCompletedAt(null);
			enrollmentRepository.save(enrollment);
		}
	}
}
