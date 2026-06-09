package com.mentora.intelligence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousPostRepository extends JpaRepository<AnonymousPost, UUID> {
	List<AnonymousPost> findByTenantId(UUID tenantId);
	long countByTenantId(UUID tenantId);
}
