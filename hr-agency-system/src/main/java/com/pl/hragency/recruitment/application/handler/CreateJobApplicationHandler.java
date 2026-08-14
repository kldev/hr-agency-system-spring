package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.CreateCandidateCommand;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.port.JobApplicationRepository;
import com.pl.hragency.recruitment.application.service.CandidateResolver;
import com.pl.hragency.recruitment.domain.event.JobApplicationCreatedEvent;
import com.pl.hragency.recruitment.domain.exception.JobPostingNotActiveException;
import com.pl.hragency.recruitment.domain.model.application.JobApplication;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.result.ApplyForPostingResult;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateJobApplicationHandler {
    private final JobPostingRepository jobPostingRepository;
    private final CandidateResolver candidateResolver;
    private final JobApplicationRepository jobApplicationRepository;
    private final EventPublisher eventPublisher;

    public CreateJobApplicationHandler(JobPostingRepository jobPostingRepository,
                                       CreateCandidateHandler createCandidateHandler,
                                       CandidateResolver candidateResolver,
                                       JobApplicationRepository jobApplicationRepository,
                                       EventPublisher eventPublisher) {

        this.jobPostingRepository = jobPostingRepository;
        this.candidateResolver = candidateResolver;
        this.jobApplicationRepository = jobApplicationRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ApplyForPostingResult handle(ExecutionContext context, CreateJobApplicationCommand command) {
        var posting = jobPostingRepository.
                findById(context.organizationId(), new JobPostingId(command.jobPostingId()))
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobPosting, command.jobPostingId()));

        if (!posting.active()) {
            throw new JobPostingNotActiveException();
        }

        var candidate = findOrCreate(context, command);

        JobApplication application;
        application = jobApplicationRepository
                .findByCandidate(candidate.id(), context.organizationId(), posting.id())
                .orElseGet(() ->
                     createApplication(context, candidate, posting, command.source())
                );

        return new ApplyForPostingResult(candidate.id().value(), application.id().value(), application.status());
    }

    private JobApplication createApplication(
            ExecutionContext context,
            Candidate candidate,
            JobPosting posting, CandidateSource source) {

        var application = JobApplication.create(
                context.organizationId(),
                posting.id(),
                candidate.id(),
                source
        );

        jobApplicationRepository.save(application);

        var event = new JobApplicationCreatedEvent(application.id().value(),
                posting.id().value(),
                posting.title(),
                application.organizationId(),
                application.candidateId().value(),
                candidate.email(),
                candidate.source(),
                context.userId(),
                context.fullName(),
                Instant.now() );
        eventPublisher.publish(event);

        return application;
    }


    private Candidate findOrCreate(ExecutionContext context, CreateJobApplicationCommand command)
    {
        var createCandidateCommand =  new CreateCandidateCommand(command.email(), command.firstName(), command.lastName(), command.phone(), command.source());
        return candidateResolver.findOrCreate(context, createCandidateCommand);
    }
}