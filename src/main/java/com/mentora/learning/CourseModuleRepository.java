package com.mentora.learning;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseModuleRepository extends JpaRepository<CourseModule, UUID> {
	List<CourseModule> findByTenantIdAndCourseId(UUID tenantId, UUID courseId);
}
