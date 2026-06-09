package com.mentora.assessment;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "questions")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Question extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Assessment assessment;

	@Column(nullable = false, length = 4000)
	private String prompt;

	@Enumerated(EnumType.STRING)
	private QuestionType questionType;

	private int points = 1;
}
