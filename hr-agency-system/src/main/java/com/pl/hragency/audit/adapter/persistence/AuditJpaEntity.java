package com.pl.hragency.audit.adapter.persistence;

import com.pl.hragency.audit.domain.model.AuditEventType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "audit_entries",
        indexes = {
                @Index(
                        name = "idx_audit_aggregate",
                        columnList = "aggregate_type, aggregate_id"
                ),
                @Index(
                        name = "idx_audit_occurred_at",
                        columnList = "occurred_at"
                ),
                @Index(
                        name = "idx_audit_actor",
                        columnList = "actor_id"
                )
        }
)
public class AuditJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 50)
    private String module;

    @Column(name = "aggregate_type", nullable = false, length = 100)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 50)
    private AuditEventType eventType;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "actor_id")
    private UUID actorId;

    @Column(name = "actor_name", length = 255)
    private String actorName;

    @Column(length = 1000)
    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String data;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    protected AuditJpaEntity() {
    }

    public AuditJpaEntity(
            UUID id,
            String module,
            String aggregateType,
            UUID aggregateId,
            UUID organizationId,
            AuditEventType eventType,
            UUID actorId,
            String actorName,
            String description,
            String data,
            Instant occurredAt) {

        this.id = id;
        this.module = module;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.organizationId = organizationId;
        this.eventType = eventType;
        this.actorId = actorId;
        this.actorName = actorName;
        this.description = description;
        this.data = data;
        this.occurredAt = occurredAt;
    }

    public UUID getId() {
        return id;
    }

    public String getModule() {
        return module;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public AuditEventType getEventType() {
        return eventType;
    }

    public UUID getActorId() {
        return actorId;
    }

    public String getActorName() {
        return actorName;
    }

    public String getDescription() {
        return description;
    }

    public String getData() {
        return data;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }
}
