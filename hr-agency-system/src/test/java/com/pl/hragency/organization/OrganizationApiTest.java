package com.pl.hragency.organization;

import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.identity.domain.model.UserRole;
import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUser;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationApiTest extends BaseApiIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void adminShouldBeAbleToCreateOrganization() {

        // given
        var adminOrganization =
                organizationFactory.create();

        var admin =
                userFactory.create(
                        adminOrganization,
                        "admin@test.com",
                        "Password123!",
                        UserRole.ADMIN
                );

        var token =
                authenticationClient.login(admin);

        var command =
                new CreateOrganizationCommand(
                        "ACME Sp. z o.o.",
                        "acme"
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
                        UserRole.RECRUITER
                );

        var token =
                authenticationClient.login(recruiter);

        var command =
                new CreateOrganizationCommand(
                        "ACME Sp. z o.o.",
                        "acme"
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
