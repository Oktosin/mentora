package com.mentora.intelligence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
	List<Recommendation> findByTenantId(UUID tenantId);
	long countByTenantId(UUID tenantId);
}
