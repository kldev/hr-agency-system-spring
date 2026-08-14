package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.ChangeJobApplicationStatusCommand;
import com.pl.hragency.recruitment.application.port.JobApplicationRepository;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.event.CandidateHiredEvent;
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
    private final JobPostingRepository jobPostingRepository;
    private final EventPublisher eventPublisher;

    public ChangeJobApplicationStatusHandler(
            JobApplicationRepository repository,
            JobPostingRepository jobPostingRepository,
            EventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.jobPostingRepository = jobPostingRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ExecutionContext context, JobApplicationId id, ChangeJobApplicationStatusCommand command) {
        var application = repository.findById(id, context.organizationId())
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobApplication, id.value())
        );

        var oldStatus = application.status();

        if (oldStatus == command.status()) {
            return;
        }

        changeStatus(application, command.status());

        repository.save(application);

        var occurredAt = Instant.now();

        publishStatusChangedEvent(
                application,
                oldStatus,
                context,
                occurredAt
        );

        if (application.status() == JobApplicationStatus.HIRED) {
            publishCandidateHiredEvent(
                    application,
                    context,
                    occurredAt
            );
        }
    }

    private void changeStatus(
            JobApplication application,
            JobApplicationStatus status
    ) {
        switch (status) {
            case ASSESSMENT -> application.startAssessment();
            case INTERVIEW -> application.scheduleInterview();
            case OFFER -> application.makeOffer();
            case REJECTED -> application.reject();
            case SCREENING -> application.startScreening();
            case WITHDRAWN -> application.withdraw();
            case HIRED -> application.hire();
            case APPLIED -> throw new IllegalStateException(
                    "Job application cannot be changed to APPLIED"
            );
        }
    }

    private void publishStatusChangedEvent(
            JobApplication application,
            JobApplicationStatus oldStatus,
            ExecutionContext context,
            Instant occurredAt
    ) {
        eventPublisher.publish(
                new JobApplicationStatusChangedEvent(
                        application.id().value(),
                        application.candidateId().value(),
                        application.organizationId(),
                        oldStatus,
                        application.status(),
                        context.userId(),
                        context.fullName(),
                        occurredAt
                )
        );
    }

    private void publishCandidateHiredEvent(
            JobApplication application,
            ExecutionContext context,
            Instant occurredAt
    ) {
        var jobPosting = jobPostingRepository.findById(
                application.organizationId(),
                application.jobPostingId()
        ).orElseThrow(() ->
                new EntityNotFoundException(
                        EntityType.JobPosting,
                        application.jobPostingId()
                )
        );

        eventPublisher.publish(
                new CandidateHiredEvent(
                        application.candidateId().value(),
                        application.id().value(),
                        jobPosting.title(),
                        jobPosting.id().value(),
                        application.organizationId(),
                        context.userId(),
                        context.fullName(),
                        occurredAt
                )
        );
    }
}