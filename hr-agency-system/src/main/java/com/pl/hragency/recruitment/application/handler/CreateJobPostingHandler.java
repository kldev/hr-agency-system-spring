package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.jobdescription.api.JobDescriptionApi;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.recruitment.application.command.CreateJobPostingCommand;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.event.JobPostingCreatedEvent;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

@Service
public class CreateJobPostingHandler {
    private final JobPostingRepository repository;
    private final JobDescriptionApi  jobDescriptionApi;
    private final IdentityApi identityApi;
    private final EventPublisher  eventPublisher;

    public CreateJobPostingHandler(JobPostingRepository repository,
                                   JobDescriptionApi jobDescriptionApi,
                                   IdentityApi identityApi,
                                   EventPublisher eventPublisher) {
        this.repository = repository;
        this.jobDescriptionApi = jobDescriptionApi;
        this.identityApi = identityApi;
        this.eventPublisher = eventPublisher;
    }

    private UUID resolveRecruitmentId(ExecutionContext context, UUID recruitmentId) {
        if (recruitmentId == null && identityApi.isCurrentUserRecruiter() )
        {
            recruitmentId = context.userId();
        }

        if (recruitmentId == null) {
            throw new IllegalArgumentException("Recruiter must be assigned");
        }

        return recruitmentId;
    }

    public JobPostingId handle(ExecutionContext context, CreateJobPostingCommand command) {

        if (!jobDescriptionApi.exists(context.organizationId(), command.jobDescriptionId())){
            throw new EntityNotFoundException(EntityType.JobDescription, command.jobDescriptionId());
        }

        UUID recruitmentId = resolveRecruitmentId(context, command.recruitmentId());

        JobPosting jobPosting = JobPosting.draft(context.organizationId(),
                command.jobDescriptionId(),
                recruitmentId,
                command.title(),
                command.summary(),
                command.description(),
                command.responsibilities(),
                command.requirements(),
                command.skills(),
                command.location(),
                command.countryCode(),
                command.employmentType(),
                command.workMode(),
                new SalaryRange(command.salaryMin(), command.salaryMax(), Currency.getInstance(command.salaryCurrency()))
                );
        repository.save(jobPosting);

        UserSnapshot userSnapshot = identityApi.findUser(recruitmentId, context.organizationId()).orElse(null);

        var event = new JobPostingCreatedEvent(jobPosting.id().value(),
                context.organizationId(),
                jobPosting.title(),
                userSnapshot,
                context.userId(),
                context.fullName(),
                Instant.now());

        eventPublisher.publish(event);

        return jobPosting.id();

    }
}
