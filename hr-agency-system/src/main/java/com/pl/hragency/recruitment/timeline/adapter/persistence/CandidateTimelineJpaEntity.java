package com.pl.hragency.recruitment.timeline.adapter.persistence;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "candidate_timeline",
        indexes = {
                @Index(
                        name = "idx_candidate_timeline_candidate",
                        columnList = "organization_id, candidate_id, occurred_at DESC"
                )
        }
)
public class CandidateTimelineJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "organization_id", nullable = false, updatable = false)
    private UUID organizationId;

    @Column(name = "candidate_id", nullable = false, updatable = false)
    private UUID candidateId;

    @Column(name = "type", nullable = false, length = 50, updatable = false)
    private String type;

    @Column(name = "actor_id", updatable = false)
    private UUID actorId;

    @Column(name = "actor_name", nullable = false, length = 255, updatable = false)
    private String actorName;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "data", nullable = false, columnDefinition = "jsonb", updatable = false)
    private String data;

    protected CandidateTimelineJpaEntity() {
    }

    public CandidateTimelineJpaEntity(
            UUID id,
            UUID organizationId,
            UUID candidateId,
            String type,
            UUID actorId,
            String actorName,
            Instant occurredAt,
            String data) {

        this.id = id;
        this.organizationId = organizationId;
        this.candidateId = candidateId;
        this.type = type;
        this.actorId = actorId;
        this.actorName = actorName;
        this.occurredAt = occurredAt;
        this.data = data;
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

    public String getType() {
        return type;
    }

    public UUID getActorId() {
        return actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getData() {
        return data;
    }
}