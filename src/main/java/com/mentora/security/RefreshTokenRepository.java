package com.mentora.security;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mentora.user.UserData;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
	Optional<RefreshToken> findByTokenHashAndActiveTrue(String tokenHash);
	List<RefreshToken> findByTenantIdAndUserDataAndActiveTrue(UUID tenantId, UserData userData);
}
