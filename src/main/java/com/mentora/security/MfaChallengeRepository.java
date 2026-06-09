package com.mentora.security;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mentora.user.UserData;

public interface MfaChallengeRepository extends JpaRepository<MfaChallenge, UUID> {
	List<MfaChallenge> findByTenantIdAndUserDataAndUsedFalseOrderByCreatedATDesc(UUID tenantId, UserData userData, Pageable pageable);
}
