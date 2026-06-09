package com.mentora.assessment;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "assessments")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Assessment extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String title;

	private int durationMinutes;
	private int passMark;
	private boolean randomized;
	private boolean active;
}
