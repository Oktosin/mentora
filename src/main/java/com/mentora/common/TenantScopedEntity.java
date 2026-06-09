package com.mentora.common;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class TenantScopedEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "tenantId", nullable = false)
	private UUID tenantId;
}
