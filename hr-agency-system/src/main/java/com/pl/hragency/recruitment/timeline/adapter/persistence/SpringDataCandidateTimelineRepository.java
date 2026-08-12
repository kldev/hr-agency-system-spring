package com.pl.hragency.recruitment.timeline.adapter.persistence;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataCandidateTimelineRepository extends JpaRepository<CandidateTimelineJpaEntity, UUID> {
    Page<CandidateTimelineJpaEntity> findByOrganizationIdAndCandidateIdOrderByOccurredAtAsc(UUID organizationId, UUID candidateId, Pageable pageable);
    List<CandidateTimelineJpaEntity> findByCandidateIdOrderByOccurredAtAsc(UUID candidateId);
}
