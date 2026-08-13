package com.pl.hragency.recruitment.domain.result;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;

import java.util.UUID;

public record ApplyForPostingResult(UUID CandidateId, UUID applicationId, JobApplicationStatus status) {
}
