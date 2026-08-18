package com.pl.hragency.testsupport;

import com.pl.hragency.identity.domain.model.OrganizationRole;
import org.springframework.stereotype.Component;

@Component
public class TestOrganizationScenario {

    private final TestOrganizationFactory organizationFactory;
    private final TestUserFactory userFactory;

    public TestOrganizationScenario(
            TestOrganizationFactory organizationFactory,
            TestUserFactory userFactory
    ) {
        this.organizationFactory = organizationFactory;
        this.userFactory = userFactory;
    }

    public Scenario create() {
        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var admin = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        return new Scenario(
                organization,
                recruiter,
                admin
        );
    }

    public record Scenario(
            TestOrganization organization,
            TestUser recruiter,
            TestUser admin
    ) {
    }
}