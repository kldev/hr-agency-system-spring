package com.pl.hragency.recruitment.timeline.application.port;

import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CandidateTimelineRepository {
    void save(CandidateTimelineEntry entry);
    Page<CandidateTimelineEntry> findByCandidate(UUID organizationId, UUID candidateId, Pageable pageable);
}
