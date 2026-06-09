package com.mentora.assessment;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionAnswerRepository extends JpaRepository<SubmissionAnswer, UUID> {
}
