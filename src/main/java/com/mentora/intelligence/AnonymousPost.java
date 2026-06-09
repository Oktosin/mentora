package com.mentora.intelligence;

import java.time.Instant;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "anonymous_posts")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AnonymousPost extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 4000)
	private String challenge;

	private String category;
	private Instant submittedAt;
}
