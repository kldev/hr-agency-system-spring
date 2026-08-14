package com.pl.hragency.recruitment.timeline.application.projection;

import com.pl.hragency.recruitment.domain.event.*;
import com.pl.hragency.recruitment.timeline.application.CandidateApplicationTimelineEntryFactory;
import com.pl.hragency.recruitment.timeline.application.port.CandidateTimelineRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class ApplicationTimelineProjection {

    private final CandidateApplicationTimelineEntryFactory factory;
    private final CandidateTimelineRepository repository;

    public ApplicationTimelineProjection(CandidateApplicationTimelineEntryFactory factory,
                                       CandidateTimelineRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    @ApplicationModuleListener
    public void handle(JobApplicationCreatedEvent event) {
        repository.save(factory.from(event));
    }

    @ApplicationModuleListener
    public void handle(JobApplicationNoteCreatedEvent event) {
        repository.save(factory.from(event));
    }

    @ApplicationModuleListener
    public void handle(JobApplicationStatusChangedEvent event) {
        repository.save(factory.from(event));
    }

    @ApplicationModuleListener
    public void handle(CandidateHiredEvent event) {
        repository.save(factory.from(event));
    }

}
