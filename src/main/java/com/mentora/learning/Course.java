package com.mentora.learning;

import java.util.UUID;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Course extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String title;

	@Column(length = 2000)
	private String description;

	private String tags;
	private UUID prerequisiteCourseId;
	private boolean active;
}
