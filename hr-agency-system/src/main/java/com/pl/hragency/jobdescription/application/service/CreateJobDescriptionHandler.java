package com.pl.hragency.jobdescription.application.service;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.application.port.JobDescriptionRepository;
import com.pl.hragency.jobdescription.domain.event.JobDescriptionCreatedEvent;
import com.pl.hragency.jobdescription.domain.model.JobDescription;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Currency;


@Service
public class CreateJobDescriptionHandler {

    private final JobDescriptionRepository repository;
    private final EventPublisher eventPublisher;
    private final IdentityApi  identityApi;

    public CreateJobDescriptionHandler(
            JobDescriptionRepository repository,
            EventPublisher eventPublisher, IdentityApi identityApi) {

        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.identityApi = identityApi;
    }

    @Transactional
    public JobDescriptionId handle(
            ExecutionContext context,
            CreateJobDescriptionCommand command) {

        var salary = new SalaryRange(
                command.salaryMin(),
                command.salaryMax(),
                Currency.getInstance(command.salaryCurrency())
        );

        var jobDescription = JobDescription.create(
                context.organizationId(),
                command.companyId(),
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
                salary,
                context.userId()
        );

        repository.save(jobDescription);

        var userSnapshot = identityApi.findUser(jobDescription.recruiterId(), context.organizationId())
                .orElseThrow();

        eventPublisher.publish(
                new JobDescriptionCreatedEvent(
                        jobDescription.id().value(),
                        jobDescription.organizationId(),
                        jobDescription.companyId(),
                        jobDescription.title(),
                        userSnapshot,
                        context.userId(),
                        context.fullName(),
                        Instant.now()
                )
        );

        return jobDescription.id();
    }
}