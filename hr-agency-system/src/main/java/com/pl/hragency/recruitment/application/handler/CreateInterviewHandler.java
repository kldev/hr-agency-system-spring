package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.CreateInterviewCommand;
import com.pl.hragency.recruitment.application.port.InterviewRepository;
import com.pl.hragency.recruitment.application.port.JobApplicationRepository;
import com.pl.hragency.recruitment.domain.event.InterviewScheduledEvent;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.domain.model.interview.Interview;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateInterviewHandler {
    private final InterviewRepository repository;
    private final JobApplicationRepository jobApplicationRepository;
    private final EventPublisher  eventPublisher;

    public CreateInterviewHandler(InterviewRepository repository,
                                  JobApplicationRepository jobApplicationRepository,
                                  EventPublisher eventPublisher) {
        this.repository = repository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.eventPublisher = eventPublisher;
    }

    public UUID handle(ExecutionContext context, JobApplicationId jobApplicationId, CreateInterviewCommand command) {
        var application = jobApplicationRepository
                .findById(jobApplicationId, context.organizationId())
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobApplication, jobApplicationId.value()));

        var scheduledAt = command.scheduledAtInstant();

        var now = Instant.now();
        var interview = Interview.plan(context.organizationId(),
                    application.candidateId().value(),
                    application.id().value(),
                    scheduledAt,
                    context.userId() );

        repository.save(interview);

        var event = new InterviewScheduledEvent(interview.id().value(),
                interview.organizationId(),
                interview.candidateId(),
                interview.applicationId(),
                context.userId(),
                scheduledAt,
                context.fullName(),
                now
                );

        eventPublisher.publish(event);

        return interview.id().value();
    }

}
