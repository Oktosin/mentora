package com.mentora.intelligence;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recommendations")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Recommendation extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private AnonymousPost anonymousPost;

	private String category;
	private String recommendationType;
	private String title;
	private String recommendedResourceId;

	@Column(length = 2000)
	private String rationale;
}
