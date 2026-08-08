package com.pl.hragency.jobdescription.application.service;

import com.pl.hragency.jobdescription.api.CreateJobDescriptionInput;
import com.pl.hragency.jobdescription.api.JobDescriptionApi;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.domain.model.EmploymentType;
import com.pl.hragency.jobdescription.domain.model.WorkMode;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobDescriptionService implements JobDescriptionApi {
    private final CreateJobDescriptionHandler createJobDescriptionHandler;

    public JobDescriptionService(CreateJobDescriptionHandler createJobDescriptionHandler) {
        this.createJobDescriptionHandler = createJobDescriptionHandler;
    }

    @Override
    public UUID create(UUID organizationId, UUID userId, CreateJobDescriptionInput request) {

       return createJobDescriptionHandler.handle(new ExecutionContext(organizationId, userId),
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
}
