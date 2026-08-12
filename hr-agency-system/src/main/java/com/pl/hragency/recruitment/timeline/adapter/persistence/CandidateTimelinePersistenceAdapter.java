package com.pl.hragency.recruitment.timeline.adapter.persistence;

import com.pl.hragency.recruitment.timeline.application.port.CandidateTimelineRepository;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class CandidateTimelinePersistenceAdapter implements CandidateTimelineRepository {

    private final SpringDataCandidateTimelineRepository repository;
    private final CandidateTimelineMapper mapper;

    public CandidateTimelinePersistenceAdapter(SpringDataCandidateTimelineRepository repository, CandidateTimelineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(CandidateTimelineEntry entry) {
        repository.save(mapper.toEntity(entry));
    }

    @Override
    public Page<CandidateTimelineEntry> findByCandidate(UUID organizationId, UUID candidateId, Pageable pageable) {
        return repository.findByOrganizationIdAndCandidateIdOrderByOccurredAtAsc(organizationId, candidateId, pageable).map(mapper::toModel);
    }
}
