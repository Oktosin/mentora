package com.mentora.learning;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LessonProgressRepository extends JpaRepository<LessonProgress, UUID> {
	Optional<LessonProgress> findByTenantIdAndLessonIdAndUserId(UUID tenantId, UUID lessonId, UUID userId);
	@Query("""
			select count(distinct progress.lesson.id)
			from LessonProgress progress
			where progress.tenantId = :tenantId
				and progress.user.id = :userId
				and progress.completed = true
				and progress.lesson.module.course.id = :courseId
			""")
	long countCompletedLessonsForCourse(@Param("tenantId") UUID tenantId, @Param("courseId") UUID courseId, @Param("userId") UUID userId);
	long countByTenantIdAndCompletedTrue(UUID tenantId);
}
