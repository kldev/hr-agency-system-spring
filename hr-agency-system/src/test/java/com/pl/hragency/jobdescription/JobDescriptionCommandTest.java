package com.pl.hragency.jobdescription;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionId;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.jobdescription.application.port.JobDescriptionRepository;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JobDescriptionCommandTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Test
    void shouldCreateJobDescription() {

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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "Java Developer",
                "Java Developer for recruitment project",
                "We are looking for an experienced Java Developer.",
                java.util.List.of(
                        "Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with frontend developers"
                ),
                java.util.List.of(
                        "3+ years of Java experience",
                        "Spring Boot experience",
                        "Good English"
                ),
                java.util.List.of(
                        "Java",
                        "Spring Boot",
                        "PostgreSQL",
                        "Docker"
                ),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000.00"),
                new BigDecimal("18000.00"),
                "PLN"
        );

        // when
        var jobDescriptionId = restTestClient
                .post()
                .uri(url("/api/job-description"))
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
        assertThat(jobDescriptionId)
                .isNotNull();

        var jobDescription = jobDescriptionRepository
                .findById(organization.id(), new JobDescriptionId(jobDescriptionId))
                .orElseThrow();

        assertThat(jobDescription.id().value())
                .isEqualTo(jobDescriptionId);

        assertThat(jobDescription.organizationId())
                .isEqualTo(organization.id());

        assertThat(jobDescription.companyId())
                .isEqualTo(companyId);

        assertThat(jobDescription.title())
                .isEqualTo("Java Developer");

        assertThat(jobDescription.summary())
                .isEqualTo(
                        "Java Developer for recruitment project"
                );

        assertThat(jobDescription.description())
                .isEqualTo(
                        "We are looking for an experienced Java Developer."
                );

        assertThat(jobDescription.responsibilities())
                .containsExactly(
                        "Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with frontend developers"
                );

        assertThat(jobDescription.requirements())
                .containsExactly(
                        "3+ years of Java experience",
                        "Spring Boot experience",
                        "Good English"
                );

        assertThat(jobDescription.skills())
                .containsExactly(
                        "Java",
                        "Spring Boot",
                        "PostgreSQL",
                        "Docker"
                );

        assertThat(jobDescription.location())
                .isEqualTo("Opole");

        assertThat(jobDescription.countryCode())
                .isEqualTo("PL");

        assertThat(jobDescription.employmentType())
                .isEqualTo(EmploymentType.FULL_TIME);

        assertThat(jobDescription.workMode())
                .isEqualTo(WorkMode.HYBRID);

        assertThat(jobDescription.salaryRange().min())
                .isEqualByComparingTo("12000.00");

        assertThat(jobDescription.salaryRange().max())
                .isEqualByComparingTo("18000.00");

        assertThat(jobDescription.salaryRange().currency().getCurrencyCode())
                .isEqualTo("PLN");
    }

    @Test
    void shouldCreateRemoteContractJobDescription() {

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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "Senior Backend Developer",
                "Remote backend development position",
                "Senior backend developer working remotely.",
                java.util.List.of(
                        "Design backend services",
                        "Develop REST APIs"
                ),
                java.util.List.of(
                        "5+ years of experience",
                        "Java experience"
                ),
                java.util.List.of(
                        "Java",
                        "Spring Boot",
                        "Kafka"
                ),
                null,
                "PL",
                EmploymentType.CONTRACT,
                WorkMode.REMOTE,
                new BigDecimal("15000"),
                new BigDecimal("22000"),
                "PLN"
        );

        // when
        var jobDescriptionId = restTestClient
                .post()
                .uri(url("/api/job-description"))
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
        assertThat(jobDescriptionId)
                .isNotNull();

        var jobDescription = jobDescriptionRepository
                .findById(organization.id(), new JobDescriptionId(jobDescriptionId))
                .orElseThrow();

        assertThat(jobDescription.employmentType())
                .isEqualTo(EmploymentType.CONTRACT);

        assertThat(jobDescription.workMode())
                .isEqualTo(WorkMode.REMOTE);

        assertThat(jobDescription.location())
                .isNull();

        assertThat(jobDescription.salaryRange().min())
                .isEqualByComparingTo("15000");

        assertThat(jobDescription.salaryRange().max())
                .isEqualByComparingTo("22000");
    }

    @Test
    void shouldNotCreateJobDescriptionForCompanyFromAnotherOrganization() {

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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "Java Developer",
                "Java Developer",
                "Java Developer position",
                java.util.List.of("Development"),
                java.util.List.of("Java"),
                java.util.List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("10000"),
                new BigDecimal("15000"),
                "PLN"
        );

        // when / then
        restTestClient
                .post()
                .uri(url("/api/job-description"))
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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "Java Developer",
                "Java Developer",
                "Java Developer position",
                java.util.List.of("Development"),
                java.util.List.of("Java"),
                java.util.List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("10000"),
                new BigDecimal("15000"),
                "PLN"
        );

        // when / then
        restTestClient
                .post()
                .uri(url("/api/job-description"))
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
    void shouldReturnBadRequestWhenJobDescriptionDataIsInvalid() {

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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "",
                "",
                "",
                java.util.List.of(),
                java.util.List.of(),
                java.util.List.of(),
                "",
                "",
                null,
                null,
                new BigDecimal("-1000"),
                new BigDecimal("-500"),
                ""
        );

        // when / then
        restTestClient
                .post()
                .uri(url("/api/job-description"))
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
    void shouldReturnBadRequestWhenEmploymentTypeIsMissing() {

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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "Java Developer",
                "Backend developer",
                "Java backend developer position",
                java.util.List.of("Development"),
                java.util.List.of("Java"),
                java.util.List.of("Java"),
                "Opole",
                "PL",
                null,
                WorkMode.HYBRID,
                new BigDecimal("10000"),
                new BigDecimal("15000"),
                "PLN"
        );

        // when / then
        restTestClient
                .post()
                .uri(url("/api/job-description"))
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
    void shouldReturnBadRequestWhenWorkModeIsMissing() {

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

        var command = new CreateJobDescriptionCommand(
                companyId,
                "Java Developer",
                "Backend developer",
                "Java backend developer position",
                java.util.List.of("Development"),
                java.util.List.of("Java"),
                java.util.List.of("Java"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                null,
                new BigDecimal("10000"),
                new BigDecimal("15000"),
                "PLN"
        );

        // when / then
        restTestClient
                .post()
                .uri(url("/api/job-description"))
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
