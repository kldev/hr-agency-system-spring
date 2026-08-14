package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.CreateJobApplicationNoteCommand;
import com.pl.hragency.recruitment.application.port.JobApplicationNoteRepository;
import com.pl.hragency.recruitment.application.port.JobApplicationRepository;
import com.pl.hragency.recruitment.domain.event.JobApplicationNoteCreatedEvent;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationNote;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateJobApplicationNoteHandler {
    private final JobApplicationNoteRepository repository;
    private final JobApplicationRepository  applicationRepository;
    private final EventPublisher  eventPublisher;

    public CreateJobApplicationNoteHandler(JobApplicationNoteRepository repository, JobApplicationRepository applicationRepository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.applicationRepository = applicationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public UUID handle(ExecutionContext context, JobApplicationId applicationId, CreateJobApplicationNoteCommand command) {

        var application = applicationRepository.findById(applicationId, context.organizationId())
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobApplication, applicationId.value()));

        var note = JobApplicationNote.create(context.organizationId(),
                context.userId(), applicationId.value(), command.content());

        repository.save(note);

        var event = new JobApplicationNoteCreatedEvent(applicationId.value(),
                application.candidateId().value(),
                context.organizationId(),
                command.content(),
                context.userId(), context.fullName(), Instant.now());

        eventPublisher.publish(event);

        return note.id().value();
    }
}
