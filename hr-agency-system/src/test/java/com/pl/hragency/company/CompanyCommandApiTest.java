package com.pl.hragency.company;

import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.company.application.command.AssignSalesOwnerCommand;
import com.pl.hragency.company.application.command.CreateCompanyCommand;
import com.pl.hragency.company.application.port.CompanyRepository;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.shared.rest.ApiError;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CompanyCommandApiTest extends BaseApiIntegrationTest {
    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private CompanyRepository companyRepository;

    private CompanyId createCompany(
            String token,
            CreateCompanyCommand command) {

        return restTestClient
                .post()
                .uri(url("/api/company"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyId.class)
                .returnResult()
                .getResponseBody();
    }

    private CreateCompanyCommand createCompanyCommand() {
        return new CreateCompanyCommand(
                "ACME Sp. z o.o.",
                "PL",
                "PL1234567890",
                "KRS0001234567",
                "Opole",
                "ul. Testowa 1",
                "45-000"
        );
    }

    @Test
    void shouldCreateCompany() {

        // given
        var organization = organizationFactory.create();

        var admin = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(admin);

        var command = createCompanyCommand();

        // when
        var companyId = createCompany(token, command);

        // then
        assertThat(companyId)
                .isNotNull();

        var company = companyRepository.findById(
                companyId, organization.id()
        ).orElse(null);

        assertThat(company)
                .isNotNull();


        assertThat(Objects.requireNonNull(company).name())
                .isEqualTo("ACME Sp. z o.o.");

        assertThat(company.taxId().value())
                .isEqualTo("PL1234567890");

        assertThat(company.address().countryCode().value())
                .isEqualTo("PL");

        assertThat(company.salesOwnerId())
                .isNull();
    }


    @Test
    void shouldAutomaticallyAssignSalesPersonWhenSalesCreatesCompany() {

        // given
        var organization = organizationFactory.create();

        var sales = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var token = authenticationClient.login(sales);

        var command = createCompanyCommand();

        // when
        var companyId = createCompany(token, command);

        // then
        assertThat(companyId)
                .isNotNull();

        var company = companyRepository.findById(
                companyId, organization.id()
        ).orElseThrow();

        assertThat(company.salesOwnerId())
                .isEqualTo(sales.id());
    }

    @Test
    void shouldNotAllowAssigningSalesPersonFromAnotherOrganization() {
        // given
        var organizationA = organizationFactory.create();
        var organizationB = organizationFactory.create();

        var adminA = userFactory.create(
                organizationA,
                "admin-a@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var salesB = userFactory.create(
                organizationB,
                "sales-b@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyA = companyFactory.create(
                organizationA.id()
        );

        var token = authenticationClient.login(adminA);

        var command = new AssignSalesOwnerCommand(
                salesB.id()
        );

        // when / then
        restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/assign-sales"
                                .formatted(companyA)
                ))
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
    void shouldCreateCompanyWithoutSalesPersonWhenAdminCreatesCompany() {

        // given
        var organization = organizationFactory.create();

        var admin = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(admin);

        var command = createCompanyCommand();

        // when
        var companyId = createCompany(token, command);

        // then
        var company = companyRepository.findById(
                companyId, organization.id()
        ).orElseThrow();

        assertThat(company).isNotNull();
        assertThat(company.salesOwnerId())
                .isNull();
    }


    @Test
    void shouldAssignSalesPersonToExistingCompany() {

        // given
        var organization = organizationFactory.create();

        var admin = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var sales = userFactory.create(
                organization,
                "sales@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(admin);

        var command = new AssignSalesOwnerCommand(
                sales.id()
        );

        // when
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
                .body(command)
                .exchange()
                .expectStatus()
                .isOk();

        // then
        var updatedCompany = companyRepository.findById(
                new CompanyId(companyId), organization.id()
        ).orElseThrow();

        assertThat(updatedCompany.salesOwnerId())
                .isEqualTo(sales.id());
    }

    @Test
    void shouldReturnBadRequestWhenCompanyAlreadyAdded() {

        // given
        var organization = organizationFactory.create();

        var admin = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(admin);

        var command = createCompanyCommand();

        // when
       createCompany(token, command);

        // second save
        restTestClient
                .post()
                .uri(url("/api/company"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiError.class);

    }
}
