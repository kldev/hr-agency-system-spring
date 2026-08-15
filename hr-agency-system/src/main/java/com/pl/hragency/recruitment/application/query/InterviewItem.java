package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.interview.InterviewStatus;

import java.time.Instant;
import java.util.UUID;

public record InterviewItem(UUID id,
                            UUID candidateId,
                            String candidateName,
                            String candidateEmail,
                            UUID applicationId,
                            InterviewStatus status,
                            String feedback,
                            Instant scheduledAt,
                            Instant createdAt,
                            UUID createdBy,
                            String createdName) {
}
