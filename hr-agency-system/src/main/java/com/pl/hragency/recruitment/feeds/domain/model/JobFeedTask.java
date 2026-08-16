package com.pl.hragency.recruitment.feeds.domain.model;

import java.time.Instant;
import java.util.UUID;

public class JobFeedTask {

    private final UUID id;
    private final UUID organizationId;

    private JobFeedTaskStatus status;

    private int attempts;
    private final Instant createdAt;
    private Instant startedAt;
    private Instant completedAt;
    private String errorMessage;

    private JobFeedTask(
            UUID id,
            UUID organizationId,
            JobFeedTaskStatus status,
            int attempts,
            Instant createdAt
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.status = status;
        this.attempts = attempts;
        this.createdAt = createdAt;
    }

    public static JobFeedTask create(UUID organizationId) {
        return new JobFeedTask(
                UUID.randomUUID(),
                organizationId,
                JobFeedTaskStatus.PENDING,
                0,
                Instant.now()
        );
    }

    public static JobFeedTask rehydrate(UUID id,
                                        UUID organizationId,
                                        JobFeedTaskStatus status,
                                        int attempts,
                                        Instant createdAt,
                                        Instant startedAt,
                                        Instant completedAt,
                                        String errorMessage) {
        var task = new JobFeedTask(id, organizationId, status, attempts,createdAt);
        task.startedAt = startedAt;
        task.completedAt = completedAt;
        task.errorMessage = errorMessage;
        return task;
    }

    public void start() {
        if (status != JobFeedTaskStatus.PENDING) {
            throw new IllegalStateException(
                    "Only pending task can be started"
            );
        }

        status = JobFeedTaskStatus.PROCESSING;
        startedAt = Instant.now();
        attempts++;
        errorMessage = null;
    }

    public void complete() {
        if (status != JobFeedTaskStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only processing task can be completed"
            );
        }

        status = JobFeedTaskStatus.COMPLETED;
        completedAt = Instant.now();
        errorMessage = null;
    }

    public void fail(
            String errorMessage
    ) {
        if (status != JobFeedTaskStatus.PROCESSING) {
            throw new IllegalStateException(
                    "Only processing task can fail"
            );
        }

         status = JobFeedTaskStatus.FAILED;
         this.errorMessage = errorMessage;
    }

    public UUID id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public JobFeedTaskStatus status() {
        return status;
    }

    public int attempts() {
        return attempts;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant startedAt() {
        return startedAt;
    }

    public Instant completedAt() {
        return completedAt;
    }

    public String errorMessage() {
        return errorMessage;
    }
}