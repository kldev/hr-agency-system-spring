package com.pl.hragency.sales;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.domain.event.SalesOpportunityCreatedEvent;
import com.pl.hragency.sales.domain.event.SalesOpportunityStageChangedEvent;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class SalesOpportunityAuditTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldCreateAuditLogWhenSalesOpportunityIsCreated() {

        // given
        var organization = organizationFactory.create();

        var sales = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(sales);

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment Project",
                "Recruitment project for senior Java developers",
                new BigDecimal("25000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                sales.id()
        );

        // when
        var opportunityId = restTestClient
                .post()
                .uri(url("/api/sales/opportunity"))
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

        // then
        assertThat(opportunityId)
                .isNotNull();

        var auditEntries = awaitAuditEntries("SalesOpportunity",
                opportunityId, 1);

        assertThat(auditEntries)
                .hasSize(1);

        var audit = auditEntries.getFirst();

        assertThat(audit.getAggregateType())
                .isEqualTo("SalesOpportunity");

        assertThat(audit.getAggregateId())
                .isEqualTo(opportunityId);

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.CREATED);

        assertThat(audit.getModule())
                .isEqualTo("sales");

        var data = jsonMapper.readValue(
                audit.getData(),
                SalesOpportunityCreatedEvent.class
        );

        assertThat(data.opportunityId())
                .isEqualTo(opportunityId);

        assertThat(data.companyId())
                .isEqualTo(companyId);

        assertThat(data.owner())
                .isNotNull();

        assertThat(data.owner().id())
                .isEqualTo(sales.id());

        assertThat(data.owner().email())
                .isEqualTo(sales.email());
    }

    @Test
    void shouldCreateAuditLogWhenSalesOpportunityStageIsChanged() {

        // given
        var organization = organizationFactory.create();

        var sales = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(sales);

        var createCommand = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment",
                "Recruitment project",
                new BigDecimal("25000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                sales.id()
        );

        var opportunityId = restTestClient
                .post()
                .uri(url("/api/sales/opportunity"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(createCommand)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UUID.class)
                .returnResult()
                .getResponseBody();

        assertThat(opportunityId)
                .isNotNull();

        var command = new ChangeSalesOpportunityStageCommand(
                SalesOpportunityStage.QUALIFIED,
                null
        );

        // when
        restTestClient
                .patch()
                .uri(url(
                        "/api/sales/opportunity/%s/stage"
                                .formatted(opportunityId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk();

        // then
        var auditEntries = awaitAuditEntries("SalesOpportunity",
                opportunityId, 2);

        assertThat(auditEntries)
                .hasSize(2);

        var audit = auditEntries.get(1);

        assertThat(audit.getAggregateType())
                .isEqualTo("SalesOpportunity");

        assertThat(audit.getAggregateId())
                .isEqualTo(opportunityId);

        assertThat(audit.getModule())
                .isEqualTo("sales");

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.STATUS_CHANGED);

        var data = jsonMapper.readValue(
                audit.getData(),
                SalesOpportunityStageChangedEvent.class
        );

        assertThat(data.salesOpportunityId())
                .isEqualTo(opportunityId);

        assertThat(data.companyId())
                .isEqualTo(companyId);

        assertThat(data.previousStage())
                .isEqualTo(SalesOpportunityStage.NEW);

        assertThat(data.newStage())
                .isEqualTo(SalesOpportunityStage.QUALIFIED);
    }

}