package com.mentora.assessment;

import java.time.LocalDateTime;
import java.util.Collections;
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
public class AssessmentServiceImpl implements AssessmentService {

	private final AssessmentRepository assessmentRepository;
	private final QuestionRepository questionRepository;
	private final AnswerOptionRepository answerRepository;
	private final SubmissionRepository submissionRepository;
	private final SubmissionAnswerRepository submissionAnswerRepository;
	private final AssessmentResultRepository resultRepository;
	private final UserDataDAO userDataDAO;
	private final TenantContext tenantContext;

	@Override
	public Assessment create(Assessment assessment, HttpServletRequest httpServletRequest) {
		assessment.setTenantId(tenantContext.requireTenantId(httpServletRequest));
		assessment.setActive(true);
		return assessmentRepository.save(assessment);
	}

	@Override
	public List<Assessment> list(HttpServletRequest httpServletRequest) {
		return assessmentRepository.findByTenantId(tenantContext.requireTenantId(httpServletRequest));
	}

	@Override
	public List<Question> questions(UUID assessmentId, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Assessment assessment = tenantAssessment(tenantId, assessmentId);
		List<Question> questions = questionRepository.findByTenantIdAndAssessmentId(tenantId, assessmentId);
		if (assessment.isRandomized()) {
			Collections.shuffle(questions);
		}
		return questions;
	}

	@Override
	public Question addQuestion(UUID assessmentId, Question question, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Assessment assessment = tenantAssessment(tenantId, assessmentId);
		question.setTenantId(tenantId);
		question.setAssessment(assessment);
		return questionRepository.save(question);
	}

	@Override
	public AnswerOption addAnswer(UUID questionId, AnswerOption answer, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Question question = tenantQuestion(tenantId, questionId);
		answer.setTenantId(tenantId);
		answer.setQuestion(question);
		return answerRepository.save(answer);
	}

	@Override
	@Transactional
	public AssessmentResult submit(UUID assessmentId, UUID userId, SubmissionRequest requestPayload, HttpServletRequest httpServletRequest) {
		UUID tenantId = tenantContext.requireTenantId(httpServletRequest);
		Assessment assessment = tenantAssessment(tenantId, assessmentId);
		UserData user = userDataDAO.findById(userId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("User not found for tenant"));
		Submission submission = new Submission();
		submission.setTenantId(tenantId);
		submission.setAssessment(assessment);
		submission.setUser(user);
		submission.setStartedAt(requestPayload.startedAt());
		submission.setSubmittedAt(LocalDateTime.now());
		validateSubmissionWindow(assessment, submission);
		submission = submissionRepository.save(submission);

		int correct = 0;
		for (SubmittedAnswer item : requestPayload.answers()) {
			Question question = tenantQuestion(tenantId, item.questionId());
			AnswerOption selected = answerRepository.findById(item.answerOptionId())
					.filter(answer -> answer.getTenantId().equals(tenantId) && answer.getQuestion().getId().equals(question.getId()))
					.orElseThrow(() -> new IllegalArgumentException("Answer option not found for tenant"));
			SubmissionAnswer submissionAnswer = new SubmissionAnswer();
			submissionAnswer.setTenantId(tenantId);
			submissionAnswer.setSubmission(submission);
			submissionAnswer.setQuestion(question);
			submissionAnswer.setSelectedAnswer(selected);
			submissionAnswer.setCorrect(selected.isCorrect());
			submissionAnswerRepository.save(submissionAnswer);
			if (selected.isCorrect()) {
				correct++;
			}
		}

		AssessmentResult result = new AssessmentResult();
		result.setTenantId(tenantId);
		result.setSubmission(submission);
		result.setUser(user);
		result.setAssessment(assessment);
		result.setScore(correct);
		result.setTotalQuestions(requestPayload.answers().size());
		result.setPassed(correct >= assessment.getPassMark());
		return resultRepository.save(result);
	}

	private Assessment tenantAssessment(UUID tenantId, UUID assessmentId) {
		return assessmentRepository.findById(assessmentId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("Assessment not found for tenant"));
	}

	private Question tenantQuestion(UUID tenantId, UUID questionId) {
		return questionRepository.findById(questionId)
				.filter(item -> item.getTenantId().equals(tenantId))
				.orElseThrow(() -> new IllegalArgumentException("Question not found for tenant"));
	}

	private void validateSubmissionWindow(Assessment assessment, Submission submission) {
		if (assessment.getDurationMinutes() <= 0 || submission.getStartedAt() == null) {
			return;
		}
		if (submission.getSubmittedAt().isAfter(submission.getStartedAt().plusMinutes(assessment.getDurationMinutes()))) {
			throw new IllegalArgumentException("Assessment submission window has expired");
		}
	}
}
