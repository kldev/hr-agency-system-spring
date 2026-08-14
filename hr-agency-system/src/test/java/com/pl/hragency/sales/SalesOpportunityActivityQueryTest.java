package com.pl.hragency.sales;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityItem;
import com.pl.hragency.sales.domain.model.SalesActivityType;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesOpportunityActivityQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private TestSalesOpportunityFactory salesOpportunityFactory;

    @Autowired
    private TestSalesOpportunityActivityFactory activityFactory;

    @Test
    void shouldReturnOpportunityActivitiesForCurrentOrganization() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(organization.id());

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        var expectedNote =
                "Client is not interested right now. Follow up again in the future.";

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                expectedNote,
                SalesActivityType.CALL
        );

        for (var i = 0; i < 10; i++) {
            activityFactory.create(
                    organization.id(),
                    opportunityId,
                    user.id()
            );
        }

        var token = authenticationClient.login(user);

        // when
        var response = getActivities(token);

        // then
        assertThat(response)
                .isNotNull();

        assertThat(response.content())
                .hasSize(11)
                .allSatisfy(item -> {
                    assertThat(item.salesOpportunityId())
                            .isEqualTo(opportunityId);

                    assertThat(item.createdBy())
                            .isEqualTo(user.id());

                    assertThat(item.createdFullName())
                            .isNotBlank();

                    assertThat(item.createdAt())
                            .isNotNull();

                    assertThat(item.occurredAt())
                            .isNotNull();
                });

        assertThat(response.content())
                .extracting(SalesOpportunityActivityItem::note)
                .contains(expectedNote);

        assertThat(response.totalElements())
                .isEqualTo(11);
    }

    @Test
    void shouldReturnOnlyActivitiesFromCurrentOrganization() {
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

        var companyId = companyFactory.create(organization.id());
        var otherCompanyId = companyFactory.create(otherOrganization.id());

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        var otherOpportunityId = salesOpportunityFactory.create(
                otherOrganization.id(),
                otherCompanyId,
                otherUser.id()
        );

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                "Activity from current organization",
                SalesActivityType.CALL
        );

        activityFactory.create(
                otherOrganization.id(),
                otherOpportunityId,
                otherUser.id(),
                "Activity from another organization",
                SalesActivityType.EMAIL
        );

        var token = authenticationClient.login(user);

        // when
        var response = getActivities(token);

        // then
        assertThat(response.content())
                .hasSize(1);

        assertThat(response.content().getFirst().salesOpportunityId())
                .isEqualTo(opportunityId);

        assertThat(response.content().getFirst().note())
                .isEqualTo("Activity from current organization");
    }

    @Test
    void shouldReturnPagedActivities() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(organization.id());

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        for (var i = 0; i < 11; i++) {
            activityFactory.create(
                    organization.id(),
                    opportunityId,
                    user.id()
            );
        }

        var token = authenticationClient.login(user);

        // when
        var response = getActivities(
                token,
                "page=0&size=5"
        );

        // then
        assertThat(response.content())
                .hasSize(5);

        assertThat(response.page())
                .isEqualTo(0);

        assertThat(response.size())
                .isEqualTo(5);

        assertThat(response.totalElements())
                .isEqualTo(11);

        assertThat(response.totalPages())
                .isEqualTo(3);
    }

    @Test
    void shouldReturnActivitiesForSpecifiedOpportunity() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(organization.id());

        var firstOpportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        var secondOpportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        activityFactory.create(
                organization.id(),
                firstOpportunityId,
                user.id(),
                "Activity for first opportunity",
                SalesActivityType.CALL
        );

        activityFactory.create(
                organization.id(),
                secondOpportunityId,
                user.id(),
                "Activity for second opportunity",
                SalesActivityType.EMAIL
        );

        activityFactory.create(
                organization.id(),
                secondOpportunityId,
                user.id(),
                "Another activity for second opportunity",
                SalesActivityType.NOTE
        );

        var token = authenticationClient.login(user);

        // when
        var response = getActivities(
                token,
                "salesOpportunityId=%s".formatted(firstOpportunityId)
        );

        // then
        assertThat(response.content())
                .hasSize(1);

        assertThat(response.content().getFirst().salesOpportunityId())
                .isEqualTo(firstOpportunityId);

        assertThat(response.content().getFirst().note())
                .isEqualTo("Activity for first opportunity");
    }

    @Test
    void shouldReturnActivitiesForSpecifiedType() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(organization.id());

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                "Called client",
                SalesActivityType.CALL
        );

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                "Sent email",
                SalesActivityType.EMAIL
        );

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                "Another call",
                SalesActivityType.CALL
        );

        var token = authenticationClient.login(user);

        // when
        var response = getActivities(
                token,
                "type=CALL"
        );

        // then
        assertThat(response.content())
                .hasSize(2)
                .allSatisfy(item ->
                        assertThat(item.type())
                                .isEqualTo(SalesActivityType.CALL)
                );

        assertThat(response.content())
                .extracting(SalesOpportunityActivityItem::note)
                .containsExactlyInAnyOrder(
                        "Called client",
                        "Another call"
                );
    }

    @Test
    void shouldFilterActivitiesBySearch() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(organization.id());

        var opportunityId = salesOpportunityFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                "Discussed Java developer position",
                SalesActivityType.CALL
        );

        activityFactory.create(
                organization.id(),
                opportunityId,
                user.id(),
                "Sent company presentation",
                SalesActivityType.EMAIL
        );

        var token = authenticationClient.login(user);

        // when
        var response = getActivities(
                token,
                "search=JAVA"
        );

        // then
        assertThat(response.content())
                .hasSize(1);

        assertThat(response.content().getFirst().note())
                .isEqualTo("Discussed Java developer position");
    }

    private PageResponse<SalesOpportunityActivityItem> getActivities(
            String token
    ) {
        return getActivities(token, null);
    }

    private PageResponse<SalesOpportunityActivityItem> getActivities(
            String token,
            String query
    ) {
        var uri = query == null
                ? url("/api/sales/activity")
                : url("/api/sales/activity?" + query);

        return restTestClient
                .get()
                .uri(uri)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<
                                PageResponse<SalesOpportunityActivityItem>>() {
                        }
                )
                .returnResult()
                .getResponseBody();
    }
}