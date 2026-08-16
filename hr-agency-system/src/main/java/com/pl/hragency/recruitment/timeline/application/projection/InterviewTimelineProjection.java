package com.pl.hragency.recruitment.timeline.application.projection;

import com.pl.hragency.recruitment.domain.event.InterviewScheduledEvent;
import com.pl.hragency.recruitment.domain.event.InterviewStatusChangedEvent;
import com.pl.hragency.recruitment.timeline.application.application.factory.CandidateInterviewTimelineEntryFactory;
import com.pl.hragency.recruitment.timeline.application.port.CandidateTimelineRepository;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class InterviewTimelineProjection {

    private final CandidateInterviewTimelineEntryFactory factory;
    private final CandidateTimelineRepository repository;

    public InterviewTimelineProjection(CandidateInterviewTimelineEntryFactory factory,
                                       CandidateTimelineRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    @ApplicationModuleListener
    public void handle(InterviewScheduledEvent event) {
        repository.save(factory.from(event));
    }

    @ApplicationModuleListener
    public void handle(InterviewStatusChangedEvent event) {
        repository.save(factory.from(event));
    }
}
