package com.pl.hragency.testsupport;

import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.application.service.CreateJobDescriptionHandler;
import com.pl.hragency.jobdescription.domain.model.EmploymentType;
import com.pl.hragency.jobdescription.domain.model.WorkMode;
import com.pl.hragency.shared.rest.ExecutionContext;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class TestJobDescriptionFactory {

    private final CreateJobDescriptionHandler handler;
    private final Faker faker;

    public TestJobDescriptionFactory(
            CreateJobDescriptionHandler handler
    ) {
        this.handler = handler;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public UUID create(
            UUID organizationId,
            UUID companyId,
            UUID userId
    ) {
        return create(
                organizationId,
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

        var command = new CreateJobDescriptionCommand(
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
                                userId
                        ),
                        command
                )
                .value();
    }
}

