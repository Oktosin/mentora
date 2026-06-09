package com.mentora.assessment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, UUID> {
	List<AssessmentResult> findByTenantId(UUID tenantId);
}
