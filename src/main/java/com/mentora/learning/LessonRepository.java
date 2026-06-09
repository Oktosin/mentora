package com.mentora.learning;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
	List<Lesson> findByTenantIdAndModuleId(UUID tenantId, UUID moduleId);
	long countByTenantIdAndModuleCourseId(UUID tenantId, UUID courseId);
}
