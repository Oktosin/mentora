package com.mentora.assessment;

import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public interface AssessmentService {
	Assessment create(Assessment assessment, HttpServletRequest request);
	List<Assessment> list(HttpServletRequest request);
	List<Question> questions(UUID assessmentId, HttpServletRequest request);
	Question addQuestion(UUID assessmentId, Question question, HttpServletRequest request);
	AnswerOption addAnswer(UUID questionId, AnswerOption answer, HttpServletRequest request);
	AssessmentResult submit(UUID assessmentId, UUID userId, SubmissionRequest submission, HttpServletRequest request);
}
