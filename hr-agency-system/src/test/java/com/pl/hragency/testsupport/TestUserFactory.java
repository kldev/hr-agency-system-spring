package com.pl.hragency.testsupport;


import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import org.springframework.stereotype.Component;

@Component
public class TestUserFactory {

    private final IdentityApi handler;

    public TestUserFactory(IdentityApi handler) {
        this.handler = handler;
    }

    public TestUser create(
            TestOrganization organization) {

        return create(
                organization,
                "test-user@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );
    }

    public TestUser create(
            TestOrganization organization,
            String email,
            String password,
            OrganizationRole role) {

        var userId = handler.createUser(
                        email,
                        "Test",
                        "User",
                        role.toString(),
                        organization.id(),
                        password

        );

        return new TestUser(
                userId,
                organization.id(),
                organization.slug(),
                email,
                password,
                role
        );
    }
}
