package com.pl.hragency.organization;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.constants.SystemAccountNames;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.domain.model.PlatformRole;
import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.result.CreateOrganizationResult;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestOwnerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;


class OrganizationCreatedEventIntegrationTest  extends BaseRestIntegrationTest {

    @Autowired
    private TestOwnerFactory ownerFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestOrganizationFactory testOrganizationFactory;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void shouldCreateSystemAccountsWhenOrganizationCreated() {
        var admin =
                ownerFactory.create(
                        "owner@test.com",
                        "Password123!",
                        PlatformRole.OWNER
                );

        var token =
                authenticationClient.loginOwner(admin);

        var command =
                new CreateOrganizationCommand(
                        "ACME Sp. z o.o.",
                        "acme3", null
                );

        // when
        var organizationId =
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
                        .expectBody(CreateOrganizationResult.class)
                        .returnResult()
                        .getResponseBody();

        // then
        assertThat(organizationId)
                .isNotNull();

        assertThat(organizationId.id())
                .isNotNull();

        assertThat(organizationId.slug())
                .isEqualTo("acme3");

        assertThat(organizationId.name())
                .isEqualTo("ACME Sp. z o.o.");

        await()
                .atMost(Duration.ofSeconds(5))
                .pollInterval(Duration.ofSeconds(1))
                .untilAsserted(() -> {
                    var users =
                            userRepository.findByOrganizationId(
                                    organizationId.id()
                            );

                    assertThat(users)
                            .extracting(User::email)
                            .containsExactlyInAnyOrder(
                                    SystemAccountNames.SYSTEM,
                                    SystemAccountNames.INTEGRATIONS
                            );
                });
    }


}
