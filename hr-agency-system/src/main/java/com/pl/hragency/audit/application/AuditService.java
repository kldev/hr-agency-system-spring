package com.pl.hragency.audit.application;

import com.pl.hragency.audit.adapter.persistence.AuditPersistenceAdapter;
import com.pl.hragency.audit.domain.model.AuditEntry;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.api.IdentityApi;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuditService {

    private final AuditPersistenceAdapter persistenceAdapter;
    private final JsonMapper objectMapper;
    private final IdentityApi api;

    public AuditService(
            AuditPersistenceAdapter persistenceAdapter,
            JsonMapper objectMapper, IdentityApi api) {

        this.persistenceAdapter = persistenceAdapter;
        this.objectMapper = objectMapper;
        this.api = api;
    }

    public void record(
            String module,
            String aggregateType,
            UUID aggregateId,
            AuditEventType eventType,
            Object data) {

        var currentUser = api.getCurrentUser();

        var auditEntry = AuditEntry.create(
                module,
                aggregateType,
                aggregateId,
                eventType,
                currentUser != null ? currentUser.userId() : null,
                currentUser != null ? currentUser.fullName() : "",
                createDescription(eventType),
                serialize(data),
                Instant.now()
        );

        persistenceAdapter.save(auditEntry);
    }

    private String serialize(Object data) {

        if (data == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JacksonException exception) {
            throw new IllegalStateException(
                    "Cannot serialize audit data",
                    exception
            );
        }
    }

    private String createDescription(AuditEventType eventType) {

        return switch (eventType) {
            case CREATED -> "Resource created";
            case UPDATED -> "Resource updated";
            case DELETED -> "Resource deleted";
            case ASSIGNED -> "Resource assigned";
            case UNASSIGNED -> "Resource unassigned";
            case STATUS_CHANGED -> "Status changed";
            case APPROVED -> "Resource approved";
            case REJECTED -> "Resource rejected";
            case HIRED -> "Candidate hired";
            case TERMINATED -> "Employment terminated";
            case SALES_OWNER_CHANGED ->  "Sales owner changed";
        };
    }
}
