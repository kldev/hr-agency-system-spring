package com.pl.hragency.testsupport;

import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestJobApplicationScenario {

    private final TestJobDescriptionScenario jobDescriptionScenario;
    private final TestJobPostingFactory jobPostingFactory;
    private final TestJobApplicationFactory jobApplicationFactory;

    public TestJobApplicationScenario(
            TestJobDescriptionScenario jobDescriptionScenario,
            TestJobPostingFactory jobPostingFactory,
            TestJobApplicationFactory jobApplicationFactory
    ) {
        this.jobDescriptionScenario = jobDescriptionScenario;

        this.jobPostingFactory = jobPostingFactory;
        this.jobApplicationFactory = jobApplicationFactory;
    }

    public Scenario create() {
        var jdScenario = jobDescriptionScenario.create();
        var organization = jdScenario.organization();
        var recruiter = jdScenario.recruiter();
        var admin = jdScenario.admin();


        var jobDescriptionId = jdScenario.jobDescriptionId();

        var jobPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                jdScenario.companyId(),
                recruiter.id()
        );

        jobPostingFactory.updateStatus(
                organization.id(),
                recruiter.id(),
                jobPostingId,
                JobPostingStatus.PUBLISHED
        );

        var jobApplicationId = jobApplicationFactory.create(
                organization.id(),
                recruiter.id(),
                jobPostingId
        );

        return new Scenario(
                organization,
                recruiter,
                admin,
                jdScenario.companyId(),
                jobDescriptionId,
                jobPostingId,
                jobApplicationId
        );
    }

    public UUID createApplication(Scenario scenario) {
        return jobApplicationFactory.create(
                scenario.organization().id(),
                scenario.recruiter().id(),
                scenario.jobPostingId()
        );
    }

    public UUID createApplication(Scenario scenario, UUID recruiterId) {
        return jobApplicationFactory.create(
                scenario.organization().id(),
                recruiterId,
                scenario.jobPostingId()
        );
    }


    public record Scenario(
            TestOrganization organization,
            TestUser recruiter,
            TestUser admin,
            UUID companyId,
            UUID jobDescriptionId,
            UUID jobPostingId,
            UUID jobApplicationId
    ) {
    }
}