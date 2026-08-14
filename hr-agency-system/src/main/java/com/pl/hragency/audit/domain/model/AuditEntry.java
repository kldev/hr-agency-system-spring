package com.pl.hragency.audit.domain.model;



import tools.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public class AuditEntry {

    private final UUID id;
    private final String module;
    private final String aggregateType;
    private final UUID aggregateId;
    private final UUID organizationId;
    private final AuditEventType eventType;

    private final UUID actorId;
    private final String actorName;

    private final String description;
    private final String data;

    private final Instant occurredAt;

    private AuditEntry(
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

    public static AuditEntry create(
            String module,
            String aggregateType,
            UUID aggregateId,
            UUID organizationId,
            AuditEventType eventType,
            UUID actorId,
            String actorName,
            String description,
            String  data,
            Instant occurredAt) {

        return new AuditEntry(
                UUID.randomUUID(),
                module,
                aggregateType,
                aggregateId,
                organizationId,
                eventType,
                actorId,
                actorName,
                description,
                data,
                occurredAt
        );
    }

    public UUID id() {
        return id;
    }

    public String module() {
        return module;
    }

    public String aggregateType() {
        return aggregateType;
    }

    public UUID aggregateId() {
        return aggregateId;
    }

    public UUID organizationId() { return organizationId; }

    public AuditEventType eventType() {
        return eventType;
    }

    public UUID actorId() {
        return actorId;
    }

    public String actorName() {
        return actorName;
    }

    public String description() {
        return description;
    }

    public String  data() {
        return data;
    }

    public Instant occurredAt() {
        return occurredAt;
    }
}
