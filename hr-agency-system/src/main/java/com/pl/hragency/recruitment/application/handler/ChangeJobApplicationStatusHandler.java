package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.ChangeJobApplicationStatusCommand;
import com.pl.hragency.recruitment.application.port.JobApplicationRepository;
import com.pl.hragency.recruitment.domain.event.JobApplicationStatusChangedEvent;
import com.pl.hragency.recruitment.domain.model.application.JobApplication;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChangeJobApplicationStatusHandler {
    private final JobApplicationRepository repository;
    private final EventPublisher  eventPublisher;

    public ChangeJobApplicationStatusHandler(JobApplicationRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ExecutionContext context, JobApplicationId id, ChangeJobApplicationStatusCommand command) {
        JobApplication application = repository.findById(id, context.organizationId())
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobApplication, id.value()));

        JobApplicationStatus oldStatus = application.status();

        if (oldStatus == command.status()) return;

        switch (command.status()) {
            case APPLIED -> { return; }
            case ASSESSMENT -> application.startAssessment();
            case INTERVIEW -> application.scheduleInterview();
            case OFFER ->  application.makeOffer();
            case REJECTED -> application.reject();
            case SCREENING -> application.startScreening();
            case WITHDRAWN ->  application.withdraw();
            case HIRED -> application.hire();
        }

        repository.save(application);

        var event = new JobApplicationStatusChangedEvent(application.id().value(),
                application.candidateId().value(),
                application.organizationId(),
                oldStatus,
                application.status(),
                context.userId(),
                context.fullName(),
                Instant.now()
                );
        eventPublisher.publish(event);
    }
}
