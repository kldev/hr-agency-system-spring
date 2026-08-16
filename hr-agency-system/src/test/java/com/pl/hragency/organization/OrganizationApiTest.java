package com.pl.hragency.organization;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.identity.domain.model.PlatformRole;
import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestOwnerFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationApiTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOwnerFactory ownerFactory;

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void adminShouldBeAbleToCreateOrganization() {

        var admin =
                ownerFactory.create(
                        "hr-owner@test.com",
                        "Password123!",
                        PlatformRole.OWNER
                );

        var token =
                authenticationClient.loginOwner(admin);

        var command =
                new CreateOrganizationCommand(
                        "ACME Sp. z o.o.",
                        "acme", null
                );

        // when
        var response =
                restTestClient
                        .post()
                        .uri("/api/organization")
                        .header(
                                "Authorization",
                                "Bearer " + token
                        )
                        .body(command)
                        .exchange()
                        .expectStatus()
                        .isOk()
                        .expectBody()
                        .returnResult();

        // then
        assertThat(response.getResponseBody())
                .isNotNull();


    }

    @Test
    void recruiterShouldNotBeAbleToCreateOrganization() {

        // given
        var organization =
                organizationFactory.create();

        var recruiter =
                userFactory.create(
                        organization,
                        "recruiter@test.com",
                        "Password123!",
                        OrganizationRole.RECRUITER
                );

        var token =
                authenticationClient.login(recruiter);

        var command =
                new CreateOrganizationCommand(
                        "ACME Sp. z o.o.",
                        "acme", null
                );

        // when / then
        restTestClient
                .post()
                .uri("/api/organization")
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isForbidden();
    }
}
