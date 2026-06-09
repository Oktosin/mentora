package com.mentora.learning;

import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "learning")
@RequiredArgsConstructor
public class LearningController {

	private final LearningService learningService;

	@RequestMapping(value = "/courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Course createCourse(@RequestBody Course course, HttpServletRequest request) {
		return learningService.createCourse(course, request);
	}

	@RequestMapping(value = "/find-courses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<Course> courses(HttpServletRequest request) {
		return learningService.courses(request);
	}

	@RequestMapping(value = "/courses/{courseId}/modules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public CourseModule createModule(@PathVariable UUID courseId, @RequestBody CourseModule module, HttpServletRequest request) {
		return learningService.createModule(courseId, module, request);
	}

	@RequestMapping(value = "/courses/{courseId}/find-modules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<CourseModule> modules(@PathVariable UUID courseId, HttpServletRequest request) {
		return learningService.modules(courseId, request);
	}

	@RequestMapping(value = "/modules/{moduleId}/lessons", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Lesson createLesson(@PathVariable UUID moduleId, @RequestBody Lesson lesson, HttpServletRequest request) {
		return learningService.createLesson(moduleId, lesson, request);
	}

	@RequestMapping(value = "/modules/{moduleId}/find-lessons", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<Lesson> lessons(@PathVariable UUID moduleId, HttpServletRequest request) {
		return learningService.lessons(moduleId, request);
	}

	@RequestMapping(value = "/courses/{courseId}/enroll/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Enrollment enroll(@PathVariable UUID courseId, @PathVariable UUID userId, HttpServletRequest request) {
		return learningService.enroll(courseId, userId, request);
	}

	@RequestMapping(value = "/lessons/{lessonId}/progress/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public LessonProgress progress(@PathVariable UUID lessonId, @PathVariable UUID userId, @RequestBody ProgressRequest progress,
			HttpServletRequest request) {
		return learningService.progress(lessonId, userId, progress, request);
	}
}
