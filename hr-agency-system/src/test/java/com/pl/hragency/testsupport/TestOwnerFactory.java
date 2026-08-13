package com.pl.hragency.testsupport;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.domain.model.PlatformRole;
import org.springframework.stereotype.Component;

@Component
public class TestOwnerFactory {
    private final IdentityApi handler;

    public TestOwnerFactory(IdentityApi handler) {
        this.handler = handler;
    }

    public TestOwner create() {

        return create(
                "owner@test.com",
                "Password123!",
                 PlatformRole.OWNER
        );
    }

    public TestOwner create(
            String email,
            String password,
            PlatformRole role) {

        var userId = handler.createPlatformUser(
                email,
                role.toString(),
                password

        );

        return new TestOwner(
                userId,
                email,
                password,
                role
        );
    }
}
