package com.mentora.assessment;

import java.time.LocalDateTime;
import java.util.List;

public record SubmissionRequest(LocalDateTime startedAt, List<SubmittedAnswer> answers) {
}
