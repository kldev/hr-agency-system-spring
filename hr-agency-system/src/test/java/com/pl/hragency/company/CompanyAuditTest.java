package com.pl.hragency.company;

import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.audit.adapter.persistence.SpringDataAuditRepository;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.company.application.command.AssignSalesOwnerCommand;
import com.pl.hragency.company.application.command.CreateCompanyCommand;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.domain.model.UserId;
import com.pl.hragency.identity.domain.model.UserRole;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyAuditTest extends BaseApiIntegrationTest {
    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldCreateAuditLogWhenCompanyIsCreated() {
        // given
        var organization =
                organizationFactory.create();

        var user =
                userFactory.create(organization);

        // when
        var token =
                authenticationClient.login(user);

        var command = new CreateCompanyCommand("Audit Company Inc.",
                "PL",
                "VAT123123123",
                "KRS-123-123-213",
                "City", "Street 1",
                "40-999"
                );

        var response = restTestClient
                .post()
                .uri(url("/api/company"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(jsonMapper.writeValueAsString(command))
                .exchange()
                .expectStatus().isOk()
                .expectBody(CompanyId.class)
                .returnResult();

        var companyId = response.getResponseBody();

        assertThat(companyId).isNotNull();


        var auditEntries = awaitAuditEntries("Company",
                companyId.value(), 1);


        var audit = auditEntries.getFirst();

        assertThat(audit.getAggregateType())
                .isEqualTo("Company");

        assertThat(audit.getAggregateId())
                .isEqualTo(companyId.value());

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.CREATED);

        assertThat(audit.getModule())
                .isEqualTo("company");

        assertThat(audit.getData().contains("Audit Company Inc."));
        assertThat(audit.getData().contains("VAT123123123"));

    }

    @Test
    void shouldCreateAuditLogWhenSalesOwnerIsChanged() {
        // given
        var organization = organizationFactory.create();

        var admin = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                UserRole.ADMIN
        );

        var sales1 = userFactory.create(
                organization,
                "sales1@test.com",
                "Password123!",
                UserRole.SALES
        );

        var sales2 = userFactory.create(
                organization,
                "sales2@test.com",
                "Password123!",
                UserRole.SALES
        );

        var token = authenticationClient.login(admin);

        var createCommand = new CreateCompanyCommand(
                "Sales Audit Company",
                "PL",
                "VAT999999999",
                "KRS-999999999",
                "Opole",
                "Street 10",
                "45-000"
        );

        var companyId = restTestClient
                .post()
                .uri(url("/api/company"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(createCommand)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyId.class)
                .returnResult()
                .getResponseBody();

        assertThat(companyId).isNotNull();

        // when
        assignSalesOwner(
                token,
                companyId.value(),
                sales1.id()
        );

        assignSalesOwner(
                token,
                companyId.value(),
                sales2.id()
        );

        // then
        var auditEntries = awaitAuditEntries("Company", companyId.value(),3);

        assertThat(auditEntries)
                .hasSize(3);

        var audit = auditEntries.get(2);

        assertThat(audit.getAggregateType())
                .isEqualTo("Company");

        assertThat(audit.getAggregateId())
                .isEqualTo(companyId.value());

        assertThat(audit.getModule())
                .isEqualTo("company");

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.SALES_OWNER_CHANGED);

        var data = jsonMapper.readValue(
                audit.getData(),
                SalesOwnerChangedAuditData.class
        );

        assertThat(data.previousOwner())
                .isNotNull();

        assertThat(data.previousOwner().id())
                .isEqualTo(sales1.id());

        assertThat(data.previousOwner().email())
                .isEqualTo(sales1.email());

        assertThat(data.currentOwner())
                .isNotNull();

        assertThat(data.currentOwner().id())
                .isEqualTo(sales2.id());

        assertThat(data.currentOwner().email())
                .isEqualTo(sales2.email());
    }

    private void assignSalesOwner(
            String token,
            UUID companyId,
            UUID salesOwnerId) {

        restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/assign-sales"
                                .formatted(companyId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(new AssignSalesOwnerCommand(salesOwnerId))
                .exchange()
                .expectStatus()
                .isOk();
    }

    private record SalesOwnerChangedAuditData(
            UserSnapshot previousOwner,
            UserSnapshot currentOwner
    ) {
    }
}
