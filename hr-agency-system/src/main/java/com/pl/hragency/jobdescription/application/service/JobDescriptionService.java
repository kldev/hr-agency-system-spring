package com.pl.hragency.jobdescription.application.service;

import com.pl.hragency.jobdescription.api.CreateJobDescriptionInput;
import com.pl.hragency.jobdescription.api.JobDescriptionApi;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.jobdescription.application.handler.CreateJobDescriptionHandler;
import com.pl.hragency.jobdescription.application.port.JobDescriptionRepository;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobDescriptionService implements JobDescriptionApi {
    private final CreateJobDescriptionHandler createJobDescriptionHandler;
    private final JobDescriptionRepository repository;

    public JobDescriptionService(CreateJobDescriptionHandler createJobDescriptionHandler, JobDescriptionRepository repository) {
        this.createJobDescriptionHandler = createJobDescriptionHandler;
        this.repository = repository;
    }

    @Override
    public UUID create(UUID organizationId, UUID userId, CreateJobDescriptionInput request) {

       return createJobDescriptionHandler.handle(new ExecutionContext(organizationId, userId, "System"),
                new CreateJobDescriptionCommand(request.companyId(),
                        request.title(),
                        request.summary(),
                        request.description(),
                        request.responsibilities(),
                        request.requirements(),
                        request.skills(),
                        request.location(),
                        "PL",
                        EmploymentType.CONTRACT,
                        WorkMode.REMOTE,
                        request.salaryMin(),
                        request.salaryMax(),
                        "PLN"
                        )).value();
    }

    @Override
    public boolean exists(UUID organizationId, UUID id) {
        return repository.exitsById(organizationId, new JobDescriptionId(id));
    }
}
