package com.pl.hragency.testsupport;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestJobDescriptionScenario {

    private final TestOrganizationScenario organizationScenario;
    private final TestCompanyFactory companyFactory;
    private final TestJobDescriptionFactory jobDescriptionFactory;

    public TestJobDescriptionScenario(
            TestOrganizationScenario organizationScenario,
            TestCompanyFactory companyFactory,
            TestJobDescriptionFactory jobDescriptionFactory
    ) {
        this.organizationScenario = organizationScenario;
        this.companyFactory = companyFactory;
        this.jobDescriptionFactory = jobDescriptionFactory;
    }

    public Scenario create() {
        var organizationScenarioResult = organizationScenario.create();

        var organization = organizationScenarioResult.organization();
        var recruiter = organizationScenarioResult.recruiter();
        var admin = organizationScenarioResult.admin();

        var companyId = companyFactory.create(
                organization.id()
        );

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                recruiter.id()
        );

        return new Scenario(
                organization,
                recruiter,
                admin,
                companyId,
                jobDescriptionId
        );
    }

    public record Scenario(
            TestOrganization organization,
            TestUser recruiter,
            TestUser admin,
            UUID companyId,
            UUID jobDescriptionId
    ) {
    }
}

