package com.mentora.assessment;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answers")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnswerOption extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Question question;

	@Column(nullable = false, length = 2000)
	private String answerText;

	private boolean correct;
}
