package com.pl.hragency.jobdescription;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.application.query.JobDescriptionItem;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestJobDescriptionFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobDescriptionQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnJobDescriptionsForCurrentOrganization() {

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

        jobDescriptionFactory.create(
                organization.id(),
                companyId,
                "Java Developer",
                "Java backend developer",
                "Develop backend applications",
                List.of(
                        "Develop backend applications",
                        "Review pull requests"
                ),
                List.of(
                        "3+ years of Java experience"
                ),
                List.of(
                        "Java",
                        "Spring Boot"
                ),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        jobDescriptionFactory.create(
                organization.id(),
                companyId,
                "React Developer",
                "Frontend developer",
                "Develop React applications",
                List.of(
                        "Develop frontend applications"
                ),
                List.of(
                        "3+ years of React experience"
                ),
                List.of(
                        "React",
                        "TypeScript"
                ),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("10000"),
                new BigDecimal("16000"),
                "PLN",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/job-description"))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobDescriptionItem>>() {
                        }
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items)
                .isNotNull();

        assertThat(items.content())
                .isNotNull()
                .hasSize(2);

        assertThat(items.content())
                .extracting(JobDescriptionItem::title)
                .containsExactlyInAnyOrder(
                        "Java Developer",
                        "React Developer"
                );
    }

    @Test
    void shouldReturnOnlyJobDescriptionsForSelectedCompany() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var company1Id = companyFactory.create(
                organization.id()
        );

        var company2Id = companyFactory.create(
                organization.id()
        );

        jobDescriptionFactory.create(
                organization.id(),
                company1Id,
                "Java Developer",
                "Java Developer",
                "Java backend developer",
                List.of("Development"),
                List.of("Java"),
                List.of("Java", "Spring Boot"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        jobDescriptionFactory.create(
                organization.id(),
                company1Id,
                "Senior Java Developer",
                "Senior Java Developer",
                "Senior backend developer",
                List.of("Architecture"),
                List.of("Java experience"),
                List.of("Java", "Spring Boot"),
                "Opole",
                "PL",
                EmploymentType.CONTRACT,
                WorkMode.REMOTE,
                new BigDecimal("18000"),
                new BigDecimal("25000"),
                "PLN",
                user.id()
        );

        jobDescriptionFactory.create(
                organization.id(),
                company2Id,
                "React Developer",
                "React Developer",
                "Frontend developer",
                List.of("Frontend development"),
                List.of("React experience"),
                List.of("React", "TypeScript"),
                "Wrocław",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("10000"),
                new BigDecimal("16000"),
                "PLN",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url(
                        "/api/job-description/company/%s"
                                .formatted(company1Id)
                ))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<List<JobDescriptionItem>>() {
                        }
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items)
                .isNotNull()
                .hasSize(2);

        assertThat(items)
                .extracting(JobDescriptionItem::title)
                .containsExactlyInAnyOrder(
                        "Java Developer",
                        "Senior Java Developer"
                );

        assertThat(items)
                .extracting(JobDescriptionItem::companyId)
                .containsOnly(company1Id);
    }

    @Test
    void shouldNotReturnJobDescriptionsFromAnotherOrganization() {

        // given
        var organization1 = organizationFactory.create();
        var organization2 = organizationFactory.create();

        var user = userFactory.create(
                organization1,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var user2 = userFactory.create(
                organization2,
                "company2user@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var company1Id = companyFactory.create(
                organization1.id()
        );

        var company2Id = companyFactory.create(
                organization2.id()
        );

        jobDescriptionFactory.create(
                organization1.id(),
                company1Id,
                "Java Developer",
                "Java Developer",
                "Java backend developer",
                List.of("Development"),
                List.of("Java"),
                List.of("Java", "Spring Boot"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        jobDescriptionFactory.create(
                organization2.id(),
                company2Id,
                "React Developer",
                "React Developer",
                "React frontend developer",
                List.of("Frontend"),
                List.of("React"),
                List.of("React", "TypeScript"),
                "Wrocław",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("10000"),
                new BigDecimal("16000"),
                "PLN",
                user2.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/job-description"))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobDescriptionItem>>() {
                        }
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();
        assertThat(items.content())
                .isNotNull()
                .hasSize(1);

        assertThat(items.content().getFirst().organizationId())
                .isEqualTo(organization1.id());

        assertThat(items.content().getFirst().companyId())
                .isEqualTo(company1Id);

        assertThat(items.content().getFirst().title())
                .isEqualTo("Java Developer");
    }

    @Test
    void shouldReturnEmptyListWhenOrganizationHasNoJobDescriptions() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/job-description?page=0&size=50"))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobDescriptionItem>>() {
                        }
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();
        assertThat(items.content())
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenCompanyHasNoJobDescriptions() {

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

        // when
        var items = restTestClient
                .get()
                .uri(url(
                        "/api/job-description/company/%s"
                                .formatted(companyId)
                ))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<List<JobDescriptionItem>>() {
                        }
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items)
                .isNotNull()
                .isEmpty();
    }

    @Test
    void shouldNotReturnJobDescriptionsWhenCompanyBelongsToAnotherOrganization() {

        // given
        var organization1 = organizationFactory.create();
        var organization2 = organizationFactory.create();

        var user = userFactory.create(
                organization1,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var user2 = userFactory.create(
                organization2,
                "adminCompany2@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization2.id()
        );

        jobDescriptionFactory.create(
                organization2.id(),
                companyId,
                "Java Developer",
                "Java Developer",
                "Java backend developer",
                List.of("Development"),
                List.of("Java"),
                List.of("Java", "Spring Boot"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user2.id()
        );

        var token = authenticationClient.login(user);

        // when / then
        restTestClient
                .get()
                .uri(url(
                        "/api/job-description/company/%s"
                                .formatted(companyId)
                ))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    void shouldReturnBadRequestWhenCompanyIdIsInvalid() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(user);

        // when / then
        restTestClient
                .get()
                .uri(url(
                        "/api/job-description/company/not-a-uuid"
                ))
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}

