package com.mentora.learning;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public interface LearningService {
	Course createCourse(Course course, HttpServletRequest request);
	List<Course> courses(HttpServletRequest request);
	CourseModule createModule(UUID courseId, CourseModule module, HttpServletRequest request);
	List<CourseModule> modules(UUID courseId, HttpServletRequest request);
	Lesson createLesson(UUID moduleId, Lesson lesson, HttpServletRequest request);
	List<Lesson> lessons(UUID moduleId, HttpServletRequest request);
	Enrollment enroll(UUID courseId, UUID userId, HttpServletRequest request);
	LessonProgress progress(UUID lessonId, UUID userId, ProgressRequest progress, HttpServletRequest request);
}
