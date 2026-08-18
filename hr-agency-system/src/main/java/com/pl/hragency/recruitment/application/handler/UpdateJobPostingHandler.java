package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.recruitment.application.command.UpdateJobPostingCommand;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.event.JobPostingUpdatedEvent;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Currency;

@Service
public class UpdateJobPostingHandler {
    private final JobPostingRepository repository;

    private final EventPublisher eventPublisher;

    public UpdateJobPostingHandler(JobPostingRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public void handle(ExecutionContext context, JobPostingId id, UpdateJobPostingCommand command) {
        JobPosting posting = repository.findById(context.organizationId(), id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobPosting, id.value()));

        posting.updateContent(command.title(),
                command.summary(),
                command.description(),
                command.responsibilities(),
                command.requirements(),
                command.skills(),
                command.location(),
                command.countryCode(),
                command.employmentType(),
                command.workMode(),
                new SalaryRange(command.salaryMin(),
                        command.salaryMax(),
                        Currency.getInstance(command.salaryCurrency())));

        repository.update(posting);

        var event = new JobPostingUpdatedEvent(posting.id().value(),
                posting.organizationId(),
                posting.title(),
                context.userId(),
                context.fullName(),
                Instant.now());

        eventPublisher.publish(event);

    }
}
