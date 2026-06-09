package com.mentora.learning;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modules")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CourseModule extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "courseId", nullable = false)
	private Course course;

	@Column(nullable = false)
	private String title;

	private int displayOrder;
}
