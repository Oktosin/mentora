package com.mentora.assessment;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, UUID> {
	long countByTenantId(UUID tenantId);
}
