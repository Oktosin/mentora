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
@Table(name = "refreshTokens")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RefreshToken extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private UserData userData;

	@ManyToOne
	private DeviceSession deviceSession;

	@Column(nullable = false, length = 1000)
	private String tokenHash;

	private LocalDateTime expiresAt;
	private LocalDateTime revokedAt;
	private LocalDateTime rotatedAt;
	private boolean active;
}
