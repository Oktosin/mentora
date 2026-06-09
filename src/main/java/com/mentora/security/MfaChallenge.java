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
@Table(name = "mfaChallenges")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MfaChallenge extends TenantScopedEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private UserData userData;

	@Column(nullable = false)
	private String otpHash;

	private String channel;
	private LocalDateTime expiresAt;
	private LocalDateTime verifiedAt;
	private boolean used;
}
