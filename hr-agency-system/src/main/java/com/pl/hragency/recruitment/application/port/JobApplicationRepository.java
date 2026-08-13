package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.domain.model.application.JobApplication;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;

import java.util.Optional;
import java.util.UUID;

public interface JobApplicationRepository {
    void save(JobApplication jobApplication);
    Optional<JobApplication> findByCandidate(CandidateId candidateId, UUID organizationId, JobPostingId postingId);
    Optional<JobApplication> findById(JobApplicationId id, UUID organizationId);
}
