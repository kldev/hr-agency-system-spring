package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.adapter.persistence.candidate.CandidateJpaEntity;
import com.pl.hragency.recruitment.adapter.persistence.posting.JobPostingJpaEntity;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "applications",
        indexes = {
                @Index(
                        name = "idx_applications_organization_id",
                        columnList = "organization_id"
                ),
                @Index(
                        name = "idx_applications_job_posting_id",
                        columnList = "organization_id, job_posting_id"
                ),
                @Index(
                        name = "idx_applications_candidate",
                        columnList = "organization_id, candidate_id"
                ),
                @Index(
                        name = "idx_applications_status",
                        columnList = "organization_id, status"
                )
        }
)
public class JobApplicationJpaEntity {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "job_posting_id", nullable = false)
    private UUID jobPostingId;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobApplicationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CandidateSource source;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected JobApplicationJpaEntity() {
    }

    public JobApplicationJpaEntity(
            UUID id,
            UUID organizationId,
            UUID jobPostingId,
            UUID candidateId,
            JobApplicationStatus status,
            CandidateSource source,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.jobPostingId = jobPostingId;
        this.candidateId = candidateId;
        this.status = status;
        this.source = source;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getJobPostingId() {
        return jobPostingId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateFrom(JobApplicationJpaEntity source) {
        this.status = source.status;
        this.updatedAt = source.updatedAt;
    }

    public CandidateSource getSource() {
        return source;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

}

