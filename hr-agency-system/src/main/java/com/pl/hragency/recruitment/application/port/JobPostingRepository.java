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
    void create(JobPosting jobPosting);

    void update(JobPosting jobPosting);

    Optional<JobPosting> findById(
            UUID organizationId,
            JobPostingId id);

    Optional<JobPosting> findBySlug(
            UUID organizationId,
            String slug);

    Page<JobPosting> search(UUID organizationId, JobPostingListQuery query, Pageable pageable);
}
