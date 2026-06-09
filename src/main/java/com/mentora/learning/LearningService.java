package com.mentora.learning;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public interface LearningService {
	Course createCourse(Course course, HttpServletRequest httpServletRequest);
	List<Course> courses(HttpServletRequest httpServletRequest);
	CourseModule createModule(UUID courseId, CourseModule module, HttpServletRequest httpServletRequest);
	List<CourseModule> modules(UUID courseId, HttpServletRequest httpServletRequest);
	Lesson createLesson(UUID moduleId, Lesson lesson, HttpServletRequest httpServletRequest);
	List<Lesson> lessons(UUID moduleId, HttpServletRequest httpServletRequest);
	Enrollment enroll(UUID courseId, UUID userId, HttpServletRequest httpServletRequest);
	LessonProgress progress(UUID lessonId, UUID userId, ProgressRequest progress, HttpServletRequest httpServletRequest);
}
