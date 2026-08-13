package com.pl.hragency.identity;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.application.command.LoginCommand;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class LoginApiTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldLoginUser() {

        // given
        var organization =
                organizationFactory.create();

        var user =
                userFactory.create(organization);

        // when
        var token =
                authenticationClient.login(user);

        // then
        assertThat(token)
                .isNotBlank();
    }

    @Test
    void shouldRejectLoginWhenPasswordIsInvalid() {

        // given
        var organization =
                organizationFactory.create();

        var user =
                userFactory.create(
                        organization,
                        "john@test.com",
                        "CorrectPassword123!",
                        OrganizationRole.RECRUITER
                );

        // when / then
        restTestClient
                .post()
                .uri("/auth/login")
                .body(new LoginCommand(
                        organization.slug(),
                        user.email(),
                        "WrongPassword123!"
                ))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void shouldRejectLoginWhenUserDoesNotExist() {

        // given
        var organization =
                organizationFactory.create();

        // when / then
        restTestClient
                .post()
                .uri("/auth/login")
                .body(new LoginCommand(
                        organization.slug(),
                        "unknown@test.com",
                        "Password123!"
                ))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    void shouldLoginUserInCorrectOrganization() {

        // given
        var acme =
                organizationFactory.create();

        var other =
                organizationFactory.create();

        var acmeUser =
                userFactory.create(
                        acme,
                        "john@test.com",
                        "AcmePassword123!",
                        OrganizationRole.RECRUITER
                );

        var otherUser =
                userFactory.create(
                        other,
                        "john@test.com",
                        "OtherPassword123!",
                        OrganizationRole.RECRUITER
                );

        // when
        var acmeToken =
                authenticationClient.login(acmeUser);

        var otherToken =
                authenticationClient.login(otherUser);

        // then
        assertThat(acmeToken)
                .isNotBlank();

        assertThat(otherToken)
                .isNotBlank();

        assertThat(acmeToken)
                .isNotEqualTo(otherToken);
    }
}
