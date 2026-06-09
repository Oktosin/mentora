package com.mentora.audit;

import java.time.Instant;
import java.util.UUID;

import com.mentora.common.TenantScopedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "activity_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ActivityLog extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	private UUID userId;

	@Column(nullable = false)
	private String action;

	private Instant timestamp;
	private String ipAddress;

	@Column(length = 1000)
	private String device;
}
