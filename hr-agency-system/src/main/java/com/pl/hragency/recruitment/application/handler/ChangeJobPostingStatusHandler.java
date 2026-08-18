package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.ChangeJobPostingStatusCommand;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.event.JobPostingStatusUpdatedEvent;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ChangeJobPostingStatusHandler {
    private final JobPostingRepository repository;
    private final EventPublisher eventPublisher;

    public ChangeJobPostingStatusHandler(JobPostingRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ExecutionContext context, JobPostingId id, ChangeJobPostingStatusCommand command) {
        JobPosting posting = repository.findById(context.organizationId(), id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobPosting, id.value()));

        JobPostingStatus oldStatus = posting.status();

        if (oldStatus == command.status()) {
            return;
        }

        posting.updateStatus(command.status());

        repository.update(posting);


        var event = new JobPostingStatusUpdatedEvent(id.value(),
                context.organizationId(),
                oldStatus,
                posting.status(),
                context.userId(),
                context.fullName(),
                Instant.now());

        eventPublisher.publish(event);
    }
}

