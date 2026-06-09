package com.mentora.learning;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessons")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Lesson extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "moduleId", nullable = false)
	private CourseModule module;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	private LessonType lessonType;

	@Column(length = 4000)
	private String content;

	private String secureAssetKey;
	private int durationSeconds;
	private int displayOrder;
}
