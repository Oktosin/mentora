package com.mentora.assessment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionRepository extends JpaRepository<AnswerOption, UUID> {
	List<AnswerOption> findByTenantIdAndQuestionId(UUID tenantId, UUID questionId);
}
