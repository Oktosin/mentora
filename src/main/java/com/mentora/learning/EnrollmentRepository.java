package com.mentora.learning;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
	Optional<Enrollment> findByTenantIdAndCourseIdAndUserId(UUID tenantId, UUID courseId, UUID userId);
	boolean existsByTenantIdAndCourseIdAndUserIdAndCompletedAtIsNotNull(UUID tenantId, UUID courseId, UUID userId);
	long countByTenantId(UUID tenantId);
	long countByTenantIdAndCompletedAtIsNotNull(UUID tenantId);
}
