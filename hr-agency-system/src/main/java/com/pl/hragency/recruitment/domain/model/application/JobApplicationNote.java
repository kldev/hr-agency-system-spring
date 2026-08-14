package com.pl.hragency.recruitment.domain.model.application;

import java.time.Instant;
import java.util.UUID;

public class JobApplicationNote {
    private final JobApplicationNoteId id;
    private final UUID organizationId;
    private final UUID authorId;
    private final UUID applicationId;
    private final String content;
    private final Instant createdAt;

    public JobApplicationNote(JobApplicationNoteId id,
                              UUID organizationId,
                              UUID authorId,
                              UUID applicationId,
                              String content,
                              Instant createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.authorId = authorId;
        this.applicationId =applicationId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static JobApplicationNote create(
            UUID organizationId,
            UUID authorId,
            UUID applicationId,
            String content){
        return new JobApplicationNote(
                JobApplicationNoteId.newId(),
                organizationId, authorId, applicationId,content, Instant.now());
    }

    public static JobApplicationNote rehydrate(JobApplicationNoteId id,
                                               UUID organizationId,
                                               UUID authorId,
                                               UUID applicationId,
                                               String content,
                                               Instant createdAt){
        return new JobApplicationNote(
                JobApplicationNoteId.newId(),
                organizationId, authorId, applicationId,content, createdAt);
    }


    public JobApplicationNoteId id() { return id; }
    public UUID organizationId() { return organizationId; }
    public UUID authorId() { return authorId; }
    public UUID applicationId() { return applicationId; }
    public String content() { return content; }
    public Instant createdAt() { return createdAt; }
}
