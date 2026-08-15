package com.pl.hragency.recruitment.feeds.adapter.persistence;

import com.pl.hragency.recruitment.feeds.model.JobFeedTaskStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Table( name = "job_feed_tasks",
        indexes = {})
@Entity
public class JobFeedTaskJpaEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private JobFeedTaskStatus status;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "completed_at", nullable = false)
    private Instant completedAt;

    @Column(name = "next_attempt_at", nullable = false)
    private Instant nextAttemptAt;

    @Column(name = "error_message")
    private String errorMessage;

    protected JobFeedTaskJpaEntity(){}

    public JobFeedTaskJpaEntity(
            UUID id,
            UUID organizationId,
            JobFeedTaskStatus status,
            int attempts,
            Instant createdAt,
            Instant startedAt,
            Instant completedAt,
            Instant nextAttemptAt,
            String errorMessage
    ) {
       this.id = id;
       this.organizationId = organizationId;
       this.status = status;
       this.attempts = attempts;
       this.createdAt = createdAt;
       this.startedAt = startedAt;
       this.completedAt = completedAt;
       this.nextAttemptAt = nextAttemptAt;
       this.errorMessage = errorMessage;

    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public JobFeedTaskStatus getStatus() {
        return status;
    }

    public int getAttempts() {
        return attempts;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Instant getNextAttemptAt() {
        return nextAttemptAt;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
