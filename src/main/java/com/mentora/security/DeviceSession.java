package com.mentora.security;

import java.time.LocalDateTime;

import com.mentora.common.TenantScopedEntity;
import com.mentora.user.UserData;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deviceSessions")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class DeviceSession extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private UserData userData;

	private String sessionId;
	private String ipAddress;

	@Column(length = 1000)
	private String device;

	private LocalDateTime lastSeenAt;
	private LocalDateTime expiresAt;
	private boolean active;
}
