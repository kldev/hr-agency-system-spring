package com.pl.hragency.recruitment.adapter.persistence;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "application_notes",
        indexes = {
                @Index(
                        name = "idx_application_notes_application",
                        columnList = "organization_id, application_id, created_at"
                ),
                @Index(
                        name = "idx_application_notes_application",
                        columnList = "organization_id, author_id, created_at"
                )
        }
)
public class JobApplicationNoteJpaEntity {

    @Id
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "application_id", nullable = false)
    private UUID applicationId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "content", length = 500, nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected JobApplicationNoteJpaEntity() {}

    public JobApplicationNoteJpaEntity(UUID id,
                                       UUID organizationId,
                                       UUID applicationId,
                                       UUID authorId,
                                       String content,
                                       Instant createdAt) {
        this.id = id;
        this.organizationId = organizationId;
        this.applicationId = applicationId;
        this.authorId = authorId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public UUID getApplicationId() {
        return applicationId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
