package com.mentora.learning;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, UUID> {
	List<Course> findByTenantId(UUID tenantId);
}
