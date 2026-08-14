package com.pl.hragency.recruitment.timeline.application.projection;

import com.pl.hragency.recruitment.domain.event.CandidateCreatedEvent;
import com.pl.hragency.recruitment.domain.event.CandidateStatusChangedEvent;
import com.pl.hragency.recruitment.domain.event.CandidateUpdatedEvent;
import com.pl.hragency.recruitment.timeline.application.CandidateTimelineEntryFactory;
import com.pl.hragency.recruitment.timeline.application.port.CandidateTimelineRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class CandidateTimelineProjection {
    private final CandidateTimelineEntryFactory factory;
    private final CandidateTimelineRepository repository;

    public CandidateTimelineProjection(CandidateTimelineEntryFactory factory,
                                       CandidateTimelineRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    @ApplicationModuleListener
    public void handle(CandidateCreatedEvent event) {
        repository.save(factory.from(event));
    }

    @ApplicationModuleListener
    public void handle(CandidateUpdatedEvent event) {
        repository.save(factory.from(event));
    }

    @ApplicationModuleListener
    public void handle(CandidateStatusChangedEvent event) {
        repository.save(factory.from(event));
    }


}
