package com.pl.hragency.sales;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityActivityCommand;
import com.pl.hragency.sales.domain.event.SalesOpportunityActivityCreatedEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityCreatedEvent;
import com.pl.hragency.sales.domain.model.SalesActivityType;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SalesOpportunityActivityAuditTest extends BaseRestIntegrationTest {

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

    @Test
    void shouldCreateAuditLogWhenSalesOpportunityActivityIsCreated() {
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

        var auditEntries = awaitAuditEntries("SalesOpportunityActivity",
                activityId, 1);

        var audit = auditEntries.getFirst();

        assertThat(audit.getAggregateType())
                .isEqualTo("SalesOpportunityActivity");

        assertThat(audit.getAggregateId())
                .isEqualTo(activityId);

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.CREATED);

        assertThat(audit.getModule())
                .isEqualTo("sales");

        var data = jsonMapper.readValue(
                audit.getData(),
                SalesOpportunityActivityCreatedEvent.class
        );

        assertThat(data.activityId())
                .isEqualTo(activityId);

        assertThat(data.opportunityId())
                .isEqualTo(opportunityId);

        assertThat(data.occurredOn())
                .isNotNull();

        assertThat(data.createdBy())
                .isNotNull();

        assertThat(data.createdBy().id())
                .isEqualTo(user.id());

        assertThat(data.createdBy().email())
                .isEqualTo(user.email());
    }
}
