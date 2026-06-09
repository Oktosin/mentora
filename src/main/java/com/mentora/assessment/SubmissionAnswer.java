package com.mentora.assessment;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submissionAnswers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SubmissionAnswer extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Submission submission;

	@ManyToOne
	private Question question;

	@ManyToOne
	private AnswerOption selectedAnswer;

	private boolean correct;
}
