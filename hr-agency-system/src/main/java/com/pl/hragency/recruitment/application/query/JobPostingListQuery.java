package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;

import java.util.UUID;

public record JobPostingListQuery(String search, JobPostingStatus status,
                                  UUID companyId, UUID jobDescriptionId) {
    public static JobPostingListQuery published() {
        return new JobPostingListQuery(null, JobPostingStatus.PUBLISHED, null, null);
    }
}
