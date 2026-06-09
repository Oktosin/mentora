package com.mentora.assessment;

import java.time.LocalDateTime;

import com.mentora.common.TenantScopedEntity;
import com.mentora.user.UserData;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submissions")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Submission extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Assessment assessment;

	@ManyToOne
	private UserData user;

	private LocalDateTime startedAt;
	private LocalDateTime submittedAt;
}
