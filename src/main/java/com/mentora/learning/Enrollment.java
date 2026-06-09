package com.mentora.learning;

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
@Table(name = "enrollments")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Enrollment extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Course course;

	@ManyToOne
	private UserData user;

	private LocalDateTime enrolledAt;
	private LocalDateTime completedAt;
}
