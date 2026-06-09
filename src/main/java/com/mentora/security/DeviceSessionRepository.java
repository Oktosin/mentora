package com.mentora.security;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mentora.user.UserData;

public interface DeviceSessionRepository extends JpaRepository<DeviceSession, UUID> {
	List<DeviceSession> findByTenantIdAndUserDataAndActiveTrue(UUID tenantId, UserData userData);
	Optional<DeviceSession> findBySessionIdAndActiveTrue(String sessionId);
}
