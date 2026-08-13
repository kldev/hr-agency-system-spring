package com.pl.hragency.sales;

import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.sales.application.command.ChangeSalesOpportunityStageCommand;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
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

class SalesOpportunityCommandTest extends BaseApiIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private SalesOpportunityRepository salesOpportunityRepository;

    @Test
    void shouldCreateSalesOpportunity() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment Project",
                "Recruitment project for senior Java developers",
                new BigDecimal("25000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                user.id()
        );

        // when
        var salesOpportunityId = restTestClient
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
                .expectBody(SalesOpportunityId.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(salesOpportunityId)
                .isNotNull();

        var salesOpportunity = salesOpportunityRepository
                .findById(
                        organization.id(),
                        salesOpportunityId
                )
                .orElseThrow();

        assertThat(salesOpportunity.id())
                .isEqualTo(salesOpportunityId);

        assertThat(salesOpportunity.organizationId())
                .isEqualTo(organization.id());

        assertThat(salesOpportunity.companyId())
                .isEqualTo(companyId);

        assertThat(salesOpportunity.title())
                .isEqualTo("Java Recruitment Project");

        assertThat(salesOpportunity.description())
                .isEqualTo(
                        "Recruitment project for senior Java developers"
                );

        assertThat(salesOpportunity.expectedValue())
                .isEqualByComparingTo("25000.00");

        assertThat(salesOpportunity.currencyCode())
                .isEqualTo("EUR");

        assertThat(salesOpportunity.expectedCloseDate())
                .isEqualTo(LocalDate.of(2026, 12, 31));

        assertThat(salesOpportunity.salesOwnerId())
                .isEqualTo(user.id());
    }

    @Test
    void shouldCreateSalesOpportunityWithoutExpectedCloseDate() {

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

        var token = authenticationClient.login(user);

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "Backend Recruitment",
                "Recruitment of backend developers",
                new BigDecimal("15000.00"),
                "PLN",
                null,
                user.id()
        );

        // when
        var salesOpportunityId = restTestClient
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
                .expectBody(SalesOpportunityId.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(salesOpportunityId)
                .isNotNull();

        var salesOpportunity = salesOpportunityRepository
                .findById(
                        organization.id(),
                        salesOpportunityId
                )
                .orElseThrow();

        assertThat(salesOpportunity.expectedCloseDate())
                .isNull();

        assertThat(salesOpportunity.expectedValue())
                .isEqualByComparingTo("15000.00");

        assertThat(salesOpportunity.currencyCode())
                .isEqualTo("PLN");
    }

    @Test
    void shouldChangeSalesOpportunityStage() {

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

        var token = authenticationClient.login(user);

        var createCommand = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment",
                "Recruitment opportunity",
                new BigDecimal("20000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                user.id()
        );

        var opportunityId = restTestClient
                .post()
                .uri(url("/api/sales/opportunity"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(createCommand)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SalesOpportunityId.class)
                .returnResult()
                .getResponseBody();

        assertThat(opportunityId)
                .isNotNull();

        var command = new ChangeSalesOpportunityStageCommand(
                SalesOpportunityStage.WON,
                null
        );

        // when / then
        restTestClient
                .patch()
                .uri(
                        url(
                                "/api/sales/opportunity/"
                                        + opportunityId.value()
                                        + "/stage"
                        )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isNoContent();

        var salesOpportunity = salesOpportunityRepository
                .findById(
                        organization.id(),
                        opportunityId
                )
                .orElseThrow();

        assertThat(salesOpportunity.stage())
                .isEqualTo(SalesOpportunityStage.NEW);

        assertThat(salesOpportunity.lostReason())
                .isNull();
    }

    @Test
    void shouldChangeSalesOpportunityStageToLostWithReason() {

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

        var token = authenticationClient.login(user);

        var createCommand = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment",
                "Recruitment opportunity",
                new BigDecimal("30000.00"),
                "EUR",
                LocalDate.of(2026, 11, 30),
                user.id()
        );

        var opportunityId = restTestClient
                .post()
                .uri(url("/api/sales/opportunity"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(createCommand)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(SalesOpportunityId.class)
                .returnResult()
                .getResponseBody();

        var command = new ChangeSalesOpportunityStageCommand(
                SalesOpportunityStage.LOST,
                "Customer selected another recruitment agency"
        );

        // when / then
        restTestClient
                .patch()
                .uri(
                        url(
                                "/api/sales/opportunity/"
                                        + opportunityId.value()
                                        + "/stage"
                        )
                )
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isNoContent();

        var salesOpportunity = salesOpportunityRepository
                .findById(
                        organization.id(),
                        opportunityId
                )
                .orElseThrow();

        assertThat(salesOpportunity.stage())
                .isEqualTo(SalesOpportunityStage.LOST);

        assertThat(salesOpportunity.lostReason())
                .isEqualTo(
                        "Customer selected another recruitment agency"
                );
    }

    @Test
    void shouldNotCreateSalesOpportunityForCompanyFromAnotherOrganization() {

        // given
        var organization1 = organizationFactory.create();
        var organization2 = organizationFactory.create();

        var user = userFactory.create(
                organization1,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization2.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment",
                "Recruitment opportunity",
                new BigDecimal("20000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                user.id()
        );

        // when / then
        restTestClient
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
                .isForbidden();
    }

    @Test
    void shouldReturnNotFoundWhenCompanyDoesNotExist() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(user);

        var companyId = UUID.randomUUID();

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment",
                "Recruitment opportunity",
                new BigDecimal("20000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                user.id()
        );

        // when / then
        restTestClient
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
                .isNotFound();
    }

    @Test
    void shouldReturnBadRequestWhenSalesOpportunityDataIsInvalid() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "",
                "",
                new BigDecimal("-1000"),
                "",
                null,
                user.id()
        );

        // when / then
        restTestClient
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
                .isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenSalesOpportunityCompanyIsMissing() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(user);

        var command = new CreateSalesOpportunityCommand(
                null,
                "Java Recruitment",
                "Recruitment opportunity",
                new BigDecimal("20000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                user.id()
        );

        // when / then
        restTestClient
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
                .isBadRequest();
    }

    @Test
    void shouldReturnBadRequestWhenSalesOwnerIsMissing() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateSalesOpportunityCommand(
                companyId,
                "Java Recruitment",
                "Recruitment opportunity",
                new BigDecimal("20000.00"),
                "EUR",
                LocalDate.of(2026, 12, 31),
                null
        );

        // when / then
        restTestClient
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
                .isBadRequest();
    }
}