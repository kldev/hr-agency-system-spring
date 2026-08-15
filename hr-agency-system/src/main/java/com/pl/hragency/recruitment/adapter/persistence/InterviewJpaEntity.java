package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.recruitment.domain.model.interview.InterviewStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "interviews",
        indexes = {
            @Index(
                    name = "idx_interviews_organization",
                    columnList = "organization_id"
            ),
            @Index(
                    name = "idx_interviews_application",
                    columnList = "organization_id, application_id"
            ),
            @Index(
                    name = "idx_interviews_status",
                    columnList = "organization_id, status"
            ),
            @Index(
                    name = "idx_interviews_scheduled",
                    columnList = "organization_id, scheduled_at"
            )
        })
public class InterviewJpaEntity {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "feedback", length = 500)
    private String feedback;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private InterviewStatus status;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    protected InterviewJpaEntity() {
    }

    public InterviewJpaEntity(
            UUID id,
            UUID organizationId,
            UUID candidateId,
            UUID applicationId,
            String feedback,
            InterviewStatus status,
            Instant scheduledAt,
            Instant createdAt,
            Instant updatedAt,
            UUID createdBy
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.candidateId = candidateId;
        this.applicationId = applicationId;
        this.feedback = feedback;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public String getFeedback() {
        return feedback;
    }

    public InterviewStatus getStatus() {
        return status;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }
}