package com.pl.hragency.audit.adapter.persistence;

import com.pl.hragency.audit.domain.model.AuditEntry;
import org.springframework.stereotype.Component;

@Component
public class AuditPersistenceAdapter {

    private final SpringDataAuditRepository repository;

    public AuditPersistenceAdapter(
            SpringDataAuditRepository repository) {
        this.repository = repository;
    }

    public void save(AuditEntry entry) {

        var entity = new AuditJpaEntity(
                entry.id(),
                entry.module(),
                entry.aggregateType(),
                entry.aggregateId(),
                entry.eventType(),
                entry.actorId(),
                entry.actorName(),
                entry.description(),
                entry.data(),
                entry.occurredAt()
        );

        repository.save(entity);
    }
}
