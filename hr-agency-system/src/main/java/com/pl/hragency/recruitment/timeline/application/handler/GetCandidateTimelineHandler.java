package com.pl.hragency.recruitment.timeline.application.handler;
import com.pl.hragency.recruitment.timeline.application.port.CandidateTimelineRepository;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetCandidateTimelineHandler {
    private final CandidateTimelineRepository repository;

    public GetCandidateTimelineHandler(
            CandidateTimelineRepository repository) {
        this.repository = repository;
    }

    public Page<CandidateTimelineEntry> handle(
            ExecutionContext executionContext,
            UUID candidateId,
            Pageable pageable) {

        return repository.findByCandidate(
                executionContext.organizationId(),
                candidateId,
                pageable
        );
    }
}
