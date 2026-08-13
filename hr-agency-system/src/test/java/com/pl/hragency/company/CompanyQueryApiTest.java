package com.pl.hragency.company;

import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.company.application.query.CompanyListItem;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyQueryApiTest extends BaseApiIntegrationTest {
    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnCompaniesForCurrentOrganization() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "user@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        companyFactory.create(
                organization.id(),
                "ACME Sp. z o.o.",
                "PL1111111111",
                "PL",
                "REG-001",
                "Opole",
                "ul. Testowa 1",
                "45-000"
        );

        companyFactory.create(
                organization.id(),
                "Beta Sp. z o.o.",
                "PL2222222222",
                "PL",
                "REG-002",
                "Wrocław",
                "ul. Testowa 2",
                "50-000"
        );

        var token = authenticationClient.login(user);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company?page=0&size=10"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<
                        PageResponse<CompanyListItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .hasSize(2);

        assertThat(response.totalElements())
                .isEqualTo(2);

        assertThat(response.page())
                .isEqualTo(0);

        assertThat(response.size())
                .isEqualTo(10);
    }

    @Test
    void shouldReturnCompanyData() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "user@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        companyFactory.create(
                organization.id(),
                "ACME Sp. z o.o.",
                "PL1234567890",
                "PL",
                "REG-123",
                "Opole",
                "ul. Testowa 10",
                "45-000"
        );

        var token = authenticationClient.login(user);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company?page=0&size=10"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<
                        PageResponse<CompanyListItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response.content())
                .singleElement()
                .satisfies(company -> {
                    assertThat(company.name())
                            .isEqualTo("ACME Sp. z o.o.");

                    assertThat(company.taxId())
                            .isEqualTo("PL1234567890");

                    assertThat(company.registrationNumber())
                            .isEqualTo("REG-123");

                    assertThat(company.countryCode())
                            .isEqualTo("PL");

                    assertThat(company.city())
                            .isEqualTo("Opole");

                    assertThat(company.status())
                            .isNotBlank();
                });
    }

    @Test
    void shouldPaginateCompanies() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "user@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        for (int i = 1; i <= 5; i++) {
            companyFactory.create(
                    organization.id(),
                    "Company " + i,
                    "PL" + String.format("%010d", i),
                    "PL",
                    "REG-" + i,
                    "Opole",
                    "Street " + i,
                    "45-000"
            );
        }

        var token = authenticationClient.login(user);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company?page=0&size=2"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<
                        PageResponse<CompanyListItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response.content())
                .hasSize(2);

        assertThat(response.totalElements())
                .isEqualTo(5);

        assertThat(response.totalPages())
                .isEqualTo(3);

        assertThat(response.page())
                .isEqualTo(0);

        assertThat(response.size())
                .isEqualTo(2);
    }

    @Test
    void shouldSearchCompaniesByName() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "user@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        companyFactory.create(
                organization.id(),
                "ACME Poland",
                "PL1111111111",
                "PL",
                "REG-001",
                "Opole",
                "Testowa 1",
                "45-000"
        );

        companyFactory.create(
                organization.id(),
                "ACME Services",
                "PL2222222222",
                "PL",
                "REG-002",
                "Wrocław",
                "Testowa 2",
                "50-000"
        );

        companyFactory.create(
                organization.id(),
                "Beta Group",
                "PL3333333333",
                "PL",
                "REG-003",
                "Kraków",
                "Testowa 3",
                "30-000"
        );

        var token = authenticationClient.login(user);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company?page=0&size=10&search=ACME"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<
                        PageResponse<CompanyListItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response.content())
                .hasSize(2);

        assertThat(response.content())
                .extracting(CompanyListItem::name)
                .containsExactlyInAnyOrder(
                        "ACME Poland",
                        "ACME Services"
                );
    }

    @Test
    void shouldNotReturnCompaniesFromAnotherOrganization() {

        // given
        var organization1 = organizationFactory.create();
        var organization2 = organizationFactory.create();

        var user1 = userFactory.create(
                organization1,
                "user1@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        companyFactory.create(
                organization1.id(),
                "Company One",
                "PL1111111111",
                "PL",
                "REG-001",
                "Opole",
                "Testowa 1",
                "45-000"
        );

        companyFactory.create(
                organization2.id(),
                "Company Two",
                "PL2222222222",
                "PL",
                "REG-002",
                "Wrocław",
                "Testowa 2",
                "50-000"
        );

        var token = authenticationClient.login(user1);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company?page=0&size=10"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<
                        PageResponse<CompanyListItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response.totalElements())
                .isEqualTo(1);

        assertThat(response.content())
                .extracting(CompanyListItem::name)
                .containsExactly("Company One");
    }

    @Test
    void shouldReturnEmptyPageWhenNoCompaniesExist() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "user@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(user);

        // when
        var response = restTestClient
                .get()
                .uri(url("/api/company?page=0&size=10"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<
                                        PageResponse<CompanyListItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response.content())
                .isEmpty();

        assertThat(response.totalElements())
                .isZero();
    }
}
