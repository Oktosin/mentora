package com.mentora.assessment;

import java.util.UUID;

public record SubmittedAnswer(UUID questionId, UUID answerOptionId) {
}
