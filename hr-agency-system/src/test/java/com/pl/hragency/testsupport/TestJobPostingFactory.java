package com.pl.hragency.testsupport;

import com.pl.hragency.recruitment.application.command.ChangeJobPostingStatusCommand;
import com.pl.hragency.recruitment.application.command.CreateJobPostingCommand;
import com.pl.hragency.recruitment.application.handler.ChangeJobPostingStatusHandler;
import com.pl.hragency.recruitment.application.handler.CreateJobPostingHandler;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class TestJobPostingFactory {

    private final CreateJobPostingHandler handler;
    private final ChangeJobPostingStatusHandler statusHandler;
    private final Faker faker;

    public TestJobPostingFactory(
            CreateJobPostingHandler handler, ChangeJobPostingStatusHandler statusHandler
    ) {
        this.handler = handler;
        this.statusHandler = statusHandler;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID create(
            UUID organizationId,
            UUID jobDescriptionId,
            UUID companyId,
            UUID userId
    ) {
        return create(
                organizationId,
                jobDescriptionId,
                companyId,
                faker.job().title(),
                faker.lorem().sentence(),
                faker.lorem().paragraph(),
                List.of(
                        "Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with the team"
                ),
                List.of(
                        "3+ years of experience",
                        "Good communication skills"
                ),
                List.of(
                        "Java",
                        "Spring Boot",
                        "PostgreSQL"
                ),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("10000"),
                new BigDecimal("15000"),
                "PLN",
                userId
        );
    }

    public UUID create(
            UUID organizationId,
            UUID jobDescriptionId,
            UUID companyId,
            String title,
            String summary,
            String description,
            List<String> responsibilities,
            List<String> requirements,
            List<String> skills,
            String location,
            String countryCode,
            EmploymentType employmentType,
            WorkMode workMode,
            BigDecimal salaryMin,
            BigDecimal salaryMax,
            String salaryCurrency,
            UUID userId
    ) {

        var command = new CreateJobPostingCommand(
                jobDescriptionId,
                companyId,
                title,
                summary,
                description,
                responsibilities,
                requirements,
                skills,
                location,
                countryCode,
                employmentType,
                workMode,
                salaryMin,
                salaryMax,
                salaryCurrency
        );

        return handler
                .handle(
                        new ExecutionContext(
                                organizationId,
                                userId, "Test"
                        ),
                        command
                )
                .value();
    }

    public void updateStatus( UUID organizationId,
                       UUID userId,
                       UUID jobPostingId,
                       JobPostingStatus status){

        var command = new ChangeJobPostingStatusCommand(status);
        statusHandler.handle(new ExecutionContext(organizationId, userId, "Test"),
                new JobPostingId(jobPostingId),
                command);
    }
}

