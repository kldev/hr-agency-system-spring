package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface JobPostingRepository {
    void save(JobPosting jobPosting);

    Optional<JobPosting> findById(
            UUID organizationId,
            JobPostingId id);

    boolean existsById(
            UUID organizationId,
            JobPostingId id);

    int updateStatus(UUID organizationId, JobPostingId id, JobPostingStatus newStatus, Instant modifiedAt);
    int updateRecruiter(UUID organizationId, JobPostingId id, UUID recruiterId);

    Page<JobPosting> search(UUID organizationId, JobPostingListQuery query, Pageable pageable);
}
