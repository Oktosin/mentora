package com.mentora.assessment;

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
@RequestMapping(value = "assessments")
@RequiredArgsConstructor
public class AssessmentController {

	private final AssessmentService assessmentService;

	@RequestMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Assessment create(@RequestBody Assessment assessment, HttpServletRequest request) {
		return assessmentService.create(assessment, request);
	}

	@RequestMapping(value = "/find-all", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<Assessment> list(HttpServletRequest request) {
		return assessmentService.list(request);
	}

	@RequestMapping(value = "/{assessmentId}/find-questions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public List<Question> questions(@PathVariable UUID assessmentId, HttpServletRequest request) {
		return assessmentService.questions(assessmentId, request);
	}

	@RequestMapping(value = "/{assessmentId}/questions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public Question addQuestion(@PathVariable UUID assessmentId, @RequestBody Question question, HttpServletRequest request) {
		return assessmentService.addQuestion(assessmentId, question, request);
	}

	@RequestMapping(value = "/questions/{questionId}/answers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public AnswerOption addAnswer(@PathVariable UUID questionId, @RequestBody AnswerOption answer, HttpServletRequest request) {
		return assessmentService.addAnswer(questionId, answer, request);
	}

	@RequestMapping(value = "/{assessmentId}/submissions/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST, RequestMethod.GET})
	public AssessmentResult submit(@PathVariable UUID assessmentId, @PathVariable UUID userId, @RequestBody SubmissionRequest submission,
			HttpServletRequest request) {
		return assessmentService.submit(assessmentId, userId, submission, request);
	}
}
