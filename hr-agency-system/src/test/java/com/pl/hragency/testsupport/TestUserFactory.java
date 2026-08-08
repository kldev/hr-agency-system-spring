package com.pl.hragency.testsupport;


import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.domain.model.UserRole;
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
                "john.smith@test.com",
                "Password123!",
                UserRole.RECRUITER
        );
    }

    public TestUser create(
            TestOrganization organization,
            String email,
            String password,
            UserRole role) {

        var userId = handler.createUser(
                        email,
                        "John",
                        "Smith",
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
