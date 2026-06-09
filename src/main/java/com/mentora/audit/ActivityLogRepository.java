package com.mentora.audit;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {
	List<ActivityLog> findByTenantId(UUID tenantId);
	long countByTenantId(UUID tenantId);
}
