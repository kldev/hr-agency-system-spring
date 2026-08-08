package com.pl.hragency.audit.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataAuditRepository  extends JpaRepository<AuditJpaEntity, UUID> {
    List<AuditJpaEntity> findByAggregateTypeAndAggregateIdOrderByOccurredAtAsc(
            String aggregateType,
            UUID aggregateId
    );

    List<AuditJpaEntity> findByActorIdOrderByOccurredAtDesc(
            UUID actorId
    );
}
