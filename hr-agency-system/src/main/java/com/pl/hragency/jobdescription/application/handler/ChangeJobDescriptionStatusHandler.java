package com.pl.hragency.jobdescription.application.handler;

import com.pl.hragency.jobdescription.application.command.ChangeJobDescriptionStatusCommand;
import com.pl.hragency.jobdescription.application.port.JobDescriptionRepository;
import com.pl.hragency.jobdescription.domain.event.JobDescriptionStatusUpdatedEvent;
import com.pl.hragency.jobdescription.domain.exception.JobDescriptionNotFoundException;
import com.pl.hragency.jobdescription.domain.model.JobDescription;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChangeJobDescriptionStatusHandler {
    private final JobDescriptionRepository repository;
    private final EventPublisher eventPublisher;

    public ChangeJobDescriptionStatusHandler(JobDescriptionRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ExecutionContext context, JobDescriptionId id, ChangeJobDescriptionStatusCommand command) {

        JobDescription jobDescription = repository.findById(context.organizationId(), id)
                .orElseThrow( () -> new JobDescriptionNotFoundException(id.value()));

        JobDescriptionStatus oldStatus = jobDescription.status();

        if (oldStatus == command.status()) {
            return;
        }

        switch (command.status()) {
            case OPEN ->  jobDescription.open();
            case CLOSED -> jobDescription.close();
            case ON_HOLD -> jobDescription.putOnHold();
            case CANCELLED ->  jobDescription.cancel();
        }

        repository.updateStatus(context.organizationId(), id, command.status(), jobDescription.updatedAt());

        var event = new JobDescriptionStatusUpdatedEvent(
                id.value(),
                context.organizationId(),
                oldStatus,
                jobDescription.status(),
                context.userId(),
                context.fullName(),
                Instant.now());

        eventPublisher.publish(event);
    }
}
