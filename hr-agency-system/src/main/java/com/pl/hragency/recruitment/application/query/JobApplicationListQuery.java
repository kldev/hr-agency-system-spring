package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;

import java.util.UUID;

public record JobApplicationListQuery(String search, UUID companyId, JobApplicationStatus status, UUID postingId, UUID recruiterId, CandidateSource source) {
    public static JobApplicationListQuery empty() {
        return new JobApplicationListQuery(null, null, null, null, null, null);
    }
}
