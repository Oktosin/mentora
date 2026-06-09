package com.mentora.learning;

import com.mentora.common.TenantScopedEntity;
import com.mentora.user.UserData;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "lessonProgress")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LessonProgress extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private Lesson lesson;

	@ManyToOne
	private UserData user;

	private boolean completed;
	private int resumePositionSeconds;
}
