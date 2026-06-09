package com.mentora.assessment;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public interface AssessmentService {
	Assessment create(Assessment assessment, HttpServletRequest httpServletRequest);
	List<Assessment> list(HttpServletRequest httpServletRequest);
	List<Question> questions(UUID assessmentId, HttpServletRequest httpServletRequest);
	Question addQuestion(UUID assessmentId, Question question, HttpServletRequest httpServletRequest);
	AnswerOption addAnswer(UUID questionId, AnswerOption answer, HttpServletRequest httpServletRequest);
	AssessmentResult submit(UUID assessmentId, UUID userId, SubmissionRequest submission, HttpServletRequest httpServletRequest);
}
