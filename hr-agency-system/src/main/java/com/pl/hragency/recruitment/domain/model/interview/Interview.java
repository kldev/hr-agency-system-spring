package com.pl.hragency.recruitment.domain.model.interview;

import java.time.Instant;
import java.util.UUID;

public class Interview {

    private final InterviewId id;
    private final UUID organizationId;
    private final UUID candidateId;
    private final UUID applicationId;

    private InterviewStatus status;
    private Instant scheduledAt;
    private String feedback;

    private final Instant createdAt;
    private Instant updatedAt;
    private final UUID createdBy;

    private Interview(
            InterviewId id,
            UUID organizationId,
            UUID candidateId,
            UUID applicationId,
            InterviewStatus status,
            Instant scheduledAt,
            String feedback,
            Instant createdAt,
            Instant updatedAt,
            UUID createdBy
    ) {
        this.id = id;
        this.organizationId = organizationId;
        this.candidateId = candidateId;
        this.applicationId = applicationId;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.feedback = feedback;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }

    public static Interview plan(
            UUID organizationId,
            UUID candidateId,
            UUID applicationId,
            Instant scheduledAt,
            UUID createdBy
    ) {
        var now = Instant.now();

        return new Interview(
                InterviewId.newId(),
                organizationId,
                candidateId,
                applicationId,
                InterviewStatus.PLANNED,
                scheduledAt,
                null,
                now,
                now,
                createdBy
        );
    }

    public static Interview rehydrate(InterviewId id,
                                      UUID organizationId,
                                      UUID candidateId,
                                      UUID applicationId,
                                      InterviewStatus status,
                                      Instant scheduledAt,
                                      String feedback,
                                      Instant createdAt,
                                      Instant updatedAt,
                                      UUID createdBy

    ){
        return new Interview(id,
                organizationId,
                candidateId,
                applicationId,
                status,
                scheduledAt,
                feedback,
                createdAt,
                updatedAt,
                createdBy);
    }

    public void complete(String feedback) {
        requireStatus(InterviewStatus.PLANNED);

        this.status = InterviewStatus.COMPLETED;
        this.feedback = requireFeedback(feedback);
        touch();
    }

    public void cancel() {
        requireStatus(InterviewStatus.PLANNED);

        this.status = InterviewStatus.CANCELLED;
        touch();
    }

    public void markAsNoShow() {
        requireStatus(InterviewStatus.PLANNED);

        this.status = InterviewStatus.NO_SHOW;
        touch();
    }

    public void reschedule(Instant scheduledAt) {
        requireStatus(InterviewStatus.PLANNED);

        this.scheduledAt = scheduledAt;
        touch();
    }

    private void requireStatus(InterviewStatus expected) {
        if (status != expected) {
            throw new IllegalStateException(
                    "Interview must be in status %s but was %s"
                            .formatted(expected, status)
            );
        }
    }

    private void touch() {
        updatedAt = Instant.now();
    }

    private String requireFeedback(String feedback) {
        if (feedback == null || feedback.isBlank()) {
            throw new IllegalArgumentException("Interview feedback is required");
        }

        return feedback;
    }

    public InterviewId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public UUID candidateId() {
        return candidateId;
    }

    public UUID applicationId() {
        return applicationId;
    }

    public InterviewStatus status() {
        return status;
    }

    public Instant scheduledAt() {
        return scheduledAt;
    }

    public String feedback() {
        return feedback;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public UUID createdBy() {
        return createdBy;
    }
}