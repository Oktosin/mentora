package com.mentora.assessment;

import com.mentora.common.TenantScopedEntity;
import com.mentora.user.UserData;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "results")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AssessmentResult extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Submission submission;

	@ManyToOne
	private Assessment assessment;

	@ManyToOne
	private UserData user;

	private int score;
	private int totalQuestions;
	private boolean passed;
}
