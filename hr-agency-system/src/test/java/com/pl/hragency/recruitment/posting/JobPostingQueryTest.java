package com.pl.hragency.recruitment.posting;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.application.query.JobPostingItem;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobPostingQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobDescriptionScenario scenario;

    @Autowired
    private TestJobPostingFactory jobPostingFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnJobPostingsForCurrentOrganization() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        var user = test.recruiter();

        var companyId = test.companyId();

        var jobDescriptionId = test.jobDescriptionId();


        jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer - Poland",
                "Java Developer position in Poland",
                "Develop backend applications for our client",
                List.of("Develop backend applications"),
                List.of("3+ years of Java experience"),
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

        jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Développeur Java",
                "Poste de développeur Java en France",
                "Développer des applications backend",
                List.of("Développer des applications backend"),
                List.of("3 ans d'expérience Java"),
                List.of("Java", "Spring Boot"),
                "Paris",
                "FR",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("4500"),
                new BigDecimal("6000"),
                "EUR",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/recruitment/job-posting"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();
        
        assertThat(items.content())
                .isNotNull()
                .hasSize(2);

        assertThat(items.content())
                .extracting(JobPostingItem::title)
                .containsExactlyInAnyOrder(
                        "Java Developer - Poland",
                        "Développeur Java"
                );
    }

    @Test
    void shouldReturnMultipleJobPostingsForSingleJobDescription() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        var user = test.recruiter();

        var companyId = test.companyId();

        var jobDescriptionId = test.jobDescriptionId();

        var polishPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer",
                "Java Developer - Polska",
                "Dołącz do naszego zespołu backendowego.",
                List.of("Tworzenie aplikacji backendowych"),
                List.of("3+ lata doświadczenia w Javie"),
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

        var frenchPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Développeur Java",
                "Développeur Java - France",
                "Rejoignez notre équipe backend.",
                List.of("Développer des applications backend"),
                List.of("3 ans d'expérience en Java"),
                List.of("Java", "Spring Boot"),
                "Paris",
                "FR",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("4500"),
                new BigDecimal("6000"),
                "EUR",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url(
                        "/api/recruitment/job-posting?jobDescriptionId=%s"
                                .formatted(jobDescriptionId)
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items.content()).isNotNull()
                .hasSize(2);

        assertThat(items.content())
                .extracting(JobPostingItem::id)
                .containsExactlyInAnyOrder(
                        polishPostingId,
                        frenchPostingId
                );

        assertThat(items.content())
                .extracting(JobPostingItem::jobDescriptionId)
                .containsOnly(jobDescriptionId);
    }

    @Test
    void shouldFilterJobPostingsBySearch() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        var user = test.recruiter();

        var companyId = test.companyId();

        var jobDescriptionId = test.jobDescriptionId();
        jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer",
                "Java backend position",
                "Develop Java applications",
                List.of("Backend development"),
                List.of("Java experience"),
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

        jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "C# Developer",
                ".NET backend position",
                "Develop .NET applications",
                List.of("Backend development"),
                List.of("C# experience"),
                List.of("C#", ".NET"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/recruitment/job-posting?search=Java"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();
        assertThat(items.content())
                .hasSize(1)
                .extracting(JobPostingItem::title)
                .containsExactly("Java Developer");
    }

    @Test
    void shouldFilterJobPostingsByStatus() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        var user = test.recruiter();

        var companyId = test.companyId();

        var jobDescriptionId = test.jobDescriptionId();
        var draftPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer - Draft",
                "Draft posting",
                "Java backend position",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var publishedPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer - Published",
                "Published posting",
                "Java backend position",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );


        jobPostingFactory.updateStatus(organization.id(), user.id(),
                publishedPostingId, JobPostingStatus.PUBLISHED);

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url(
                        "/api/recruitment/job-posting?status=%s"
                                .formatted(JobPostingStatus.PUBLISHED)
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();

        assertThat(items.content())
                .extracting(JobPostingItem::id)
                .containsExactly(publishedPostingId)
                .doesNotContain(draftPostingId);
    }

    @Test
    void shouldFilterJobPostingsByCompany() {
        // given
        var test = scenario.create();
        var organization = test.organization();
        var user = test.admin();

        var firstCompanyId = companyFactory.create(organization.id());
        var secondCompanyId = companyFactory.create(organization.id());

        var firstJobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                firstCompanyId,
                "Java Developer",
                "Java developer",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var secondJobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                secondCompanyId,
                "C# Developer",
                "C# developer",
                ".NET backend",
                List.of("Backend"),
                List.of("C#"),
                List.of("C#", ".NET"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var firstPostingId = jobPostingFactory.create(
                organization.id(),
                firstJobDescriptionId,
                firstCompanyId,
                "Java Developer",
                "Java position",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var secondPostingId = jobPostingFactory.create(
                organization.id(),
                secondJobDescriptionId,
                secondCompanyId,
                "C# Developer",
                "C# position",
                ".NET backend",
                List.of("Backend"),
                List.of("C#"),
                List.of("C#", ".NET"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url(
                        "/api/recruitment/job-posting?companyId=%s"
                                .formatted(firstCompanyId)
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();

        assertThat(items.content())
                .hasSize(1)
                .extracting(JobPostingItem::id)
                .containsExactly(firstPostingId)
                .doesNotContain(secondPostingId);
    }

    @Test
    void shouldCombineJobPostingFilters() {
        // given
        // given
        var test = scenario.create();
        var organization = test.organization();
        var user = test.admin();

        var companyId = companyFactory.create(organization.id());
        var anotherCompanyId = companyFactory.create(organization.id());

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                "Java Developer",
                "Java developer",
                "Java backend",
                List.of("Backend"),
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

        var anotherJobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                anotherCompanyId,
                "Java Developer",
                "Java developer",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var matchingPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer",
                "Java position",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        jobPostingFactory.create(
                organization.id(),
                anotherJobDescriptionId,
                anotherCompanyId,
                "Java Developer",
                "Java position",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(builder -> builder
                    .path("/api/recruitment/job-posting")
                    .queryParam("search", "Java")
                    .queryParam("companyId", companyId)
                    .queryParam("jobDescriptionId", jobDescriptionId)
                    .build())
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();

        assertThat(items.content())
                .hasSize(1)
                .extracting(JobPostingItem::id)
                .containsExactly(matchingPostingId);
    }

    @Test
    void shouldReturnEmptyPageWhenNoJobPostingMatchesFilter() {
        // given
        var test = scenario.create();
        var organization = test.organization();
        var user = test.admin();
        var companyId = companyFactory.create(organization.id());

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                "Java Developer",
                "Java developer",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                "Java Developer",
                "Java position",
                "Java backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user.id()
        );

        var token = authenticationClient.login(user);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/recruitment/job-posting?search=Python"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();
        assertThat(items.content()).isEmpty();
        assertThat(items.totalElements()).isZero();
    }

    @Test
    void shouldReturnOnlyJobPostingsFromCurrentOrganization() {
        // given
        // given
        var testA = scenario.create();
        var testB = scenario.create();

        var organization1 = testA.organization();
        var organization2 = testB.organization();

        var user1 = testA.admin();

        var user2 = testB.admin();

        var company1 = companyFactory.create(organization1.id());
        var company2 = companyFactory.create(organization2.id());

        var description1 = jobDescriptionFactory.create(
                organization1.id(),
                company1,
                "Java Developer",
                "Java developer",
                "Backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user1.id()
        );

        var description2 = jobDescriptionFactory.create(
                organization2.id(),
                company2,
                "Java Developer",
                "Java developer",
                "Backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user2.id()
        );

        var posting1 = jobPostingFactory.create(
                organization1.id(),
                description1,
                company1,
                "Organization 1 Posting",
                "Posting 1",
                "Backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user1.id()
        );

        var posting2 = jobPostingFactory.create(
                organization2.id(),
                description2,
                company2,
                "Organization 2 Posting",
                "Posting 2",
                "Backend",
                List.of("Backend"),
                List.of("Java"),
                List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN",
                user2.id()
        );

        var token = authenticationClient.login(user1);

        // when
        var items = restTestClient
                .get()
                .uri(url("/api/recruitment/job-posting"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<PageResponse<JobPostingItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(items).isNotNull();

        assertThat(items.content())
                .extracting(JobPostingItem::id)
                .containsExactly(posting1)
                .doesNotContain(posting2);
    }
}
