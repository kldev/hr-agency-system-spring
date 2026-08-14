package com.pl.hragency.sales;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityActivityCommand;
import com.pl.hragency.sales.application.port.SalesOpportunityActivityRepository;
import com.pl.hragency.sales.domain.model.SalesActivityType;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesOpportunityActivityCommandTest extends BaseRestIntegrationTest {
    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private TestSalesOpportunityFactory salesOpportunityFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private SalesOpportunityActivityRepository salesActivityRepository;

    @Test
    void shouldCreateSalesOpportunityActivity() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var opportunityId = salesOpportunityFactory.create(
                organization.id(), companyId, user.id());

        var command = new CreateSalesOpportunityActivityCommand(
                "Client is not interested right now. Follow up again in the future.",
                SalesActivityType.CALL);

        var token = authenticationClient.login(user);
        // when
        var activityId =createActivity(opportunityId, command, token);

        // then
        assertThat(activityId)
                .isNotNull();

        var salesActivity = salesActivityRepository
                .findById(
                        activityId,
                        organization.id()
                )
                .orElseThrow();

        assertThat(salesActivity.id().value())
                .isEqualTo(activityId);

        assertThat(salesActivity.organizationId())
                .isEqualTo(organization.id());

        assertThat(salesActivity.type())
                .isEqualTo(SalesActivityType.CALL);

        assertThat(salesActivity.note())
                .isEqualTo("Client is not interested right now. Follow up again in the future.");

        assertThat(salesActivity.createdAt())
                .isNotNull();

        assertThat(salesActivity.occurredAt())
                .isNotNull();
    }

    @Test
    void shouldNotCreateActivityForNonExistingOpportunity() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var command = new CreateSalesOpportunityActivityCommand(
                "Client is interested in the offer.",
                SalesActivityType.CALL
        );

        var nonExistingOpportunityId = UUID.randomUUID();

        var token = authenticationClient.login(user);

        // when / then
        restTestClient
                .post()
                .uri(url("/api/sales/activity/%s"
                        .formatted(nonExistingOpportunityId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void shouldNotCreateActivityForOpportunityFromAnotherOrganization() {
        // given
        var organization = organizationFactory.create();
        var otherOrganization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var otherUser = userFactory.create(
                otherOrganization,
                "other-sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                otherOrganization.id()
        );

        var opportunityId = salesOpportunityFactory.create(
                otherOrganization.id(),
                companyId,
                otherUser.id()
        );

        var command = new CreateSalesOpportunityActivityCommand(
                "This activity must not be created.",
                SalesActivityType.CALL
        );

        var token = authenticationClient.login(user);

        // when / then
        restTestClient
                .post()
                .uri(url("/api/sales/activity/%s"
                        .formatted(opportunityId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void shouldRejectActivityWithBlankNote() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        var command = new CreateSalesOpportunityActivityCommand(
                "   ",
                SalesActivityType.CALL
        );

        var token = authenticationClient.login(user);

        // when / then
        restTestClient
                .post()
                .uri(url("/api/sales/activity/%s"
                        .formatted(opportunityId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldRejectActivityWithoutType() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        var command = new CreateSalesOpportunityActivityCommand(
                "Client requested another call next week.",
                null
        );

        var token = authenticationClient.login(user);

        // when / then
        restTestClient
                .post()
                .uri(url("/api/sales/activity/%s"
                        .formatted(opportunityId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private UUID createActivity(
            UUID opportunityId,
            CreateSalesOpportunityActivityCommand command,
            String token
    ) {
        return restTestClient
                .post()
                .uri(url("/api/sales/activity/%s"
                        .formatted(opportunityId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UUID.class)
                .returnResult()
                .getResponseBody();
    }
}
