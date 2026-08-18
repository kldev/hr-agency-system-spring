package com.pl.hragency.recruitment.posting;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.application.command.ChangeJobPostingStatusCommand;
import com.pl.hragency.recruitment.application.command.CreateJobPostingCommand;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.testsupport.*;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JobPostingCommandTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobDescriptionScenario scenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    private JobPostingId createJobPosting(
            TestUser user,
            UUID jobDescriptionId
    ) {
        var command = new CreateJobPostingCommand(
                jobDescriptionId,
                user.id(),
                "Java Developer",
                "Java Developer for recruitment project",
                "We are looking for an experienced Java Developer.",
                java.util.List.of("Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with frontend developers"),
                java.util.List.of("3+ years of Java experience",
                        "Spring Boot experience",
                        "Good English"),
                java.util.List.of(   "Java",
                        "Spring Boot",
                        "PostgreSQL",
                        "Docker"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000.00"),
                new BigDecimal("18000.00"),
                "PLN"
        );

        var token = authenticationClient.login(user);

        var postingId = restTestClient.post()
                .uri(url("/api/recruitment/job-posting"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UUID.class)
                .returnResult()
                .getResponseBody();

        assertThat(postingId).isNotNull();

        restTestClient.put()
                .uri(url("/api/recruitment/job-posting/%s/status".formatted(postingId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new ChangeJobPostingStatusCommand(JobPostingStatus.PUBLISHED))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ApiResult.class);

        return new JobPostingId(postingId);
    }



    @Test
    void shouldCreateJobPosting() {

        // given
        var test = scenario.create();
        var organization = test.organization();

        // when
        var jobPostingId = createJobPosting(test.recruiter(), test.jobDescriptionId());

        // then
        var jobPosting = jobPostingRepository
                .findById(organization.id(), jobPostingId)
                .orElseThrow();

        assertThat(jobPosting.id())
                .isEqualTo(jobPostingId);

        assertThat(jobPosting.organizationId())
                .isEqualTo(organization.id());


        assertThat(jobPosting.title())
                .isEqualTo("Java Developer");

        assertThat(jobPosting.summary())
                .isEqualTo(
                        "Java Developer for recruitment project"
                );

        assertThat(jobPosting.description())
                .isEqualTo(
                        "We are looking for an experienced Java Developer."
                );

        assertThat(jobPosting.responsibilities())
                .containsExactly(
                        "Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with frontend developers"
                );

        assertThat(jobPosting.requirements())
                .containsExactly(
                        "3+ years of Java experience",
                        "Spring Boot experience",
                        "Good English"
                );

        assertThat(jobPosting.skills())
                .containsExactly(
                        "Java",
                        "Spring Boot",
                        "PostgreSQL",
                        "Docker"
                );

        assertThat(jobPosting.location())
                .isEqualTo("Opole");

        assertThat(jobPosting.countryCode())
                .isEqualTo("PL");

        assertThat(jobPosting.employmentType())
                .isEqualTo(EmploymentType.FULL_TIME);

        assertThat(jobPosting.workMode())
                .isEqualTo(WorkMode.HYBRID);

        assertThat(jobPosting.salaryRange().min())
                .isEqualByComparingTo("12000.00");

        assertThat(jobPosting.salaryRange().max())
                .isEqualByComparingTo("18000.00");

        assertThat(jobPosting.salaryRange().currency().getCurrencyCode())
                .isEqualTo("PLN");

        assertThat(jobPosting.recruiterId())
                .isEqualTo(test.recruiter().id());

        assertThat(jobPosting.organizationSlug()).isNotNull();
        assertThat(jobPosting.slug()).isNotNull().contains("java-developer");

    }

    @Test
    void shouldChangeJobPostingStatus() {

        // given
        var test = scenario.create();
        var organization = test.organization();

        // when
        var jobPostingId = createJobPosting(test.recruiter(), test.jobDescriptionId());

        var token = authenticationClient.login(test.recruiter());

        var command = new ChangeJobPostingStatusCommand(
                JobPostingStatus.PUBLISHED
        );

        // when
        restTestClient
                .put()
                .uri(url("/api/recruitment/job-posting/%s/status".formatted(jobPostingId.value())))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk();

        // then
        var posting = jobPostingRepository
                .findById(organization.id(), jobPostingId)
                .orElseThrow();

        assertThat(posting.status())
                .isEqualTo(JobPostingStatus.PUBLISHED);
    }

    @Test
    void shouldClosePublishedJobPosting() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        // when
        var jobPostingId = createJobPosting(test.recruiter(), test.jobDescriptionId());
        var token = authenticationClient.login(test.recruiter());

        restTestClient
                .put()
                .uri(url("/api/recruitment/job-posting/%s/status".formatted(jobPostingId.value())))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new ChangeJobPostingStatusCommand(
                        JobPostingStatus.PUBLISHED
                ))
                .exchange()
                .expectStatus()
                .isOk();

        // when
        restTestClient
                .put()
                .uri(url("/api/recruitment/job-posting/%s/status".formatted(jobPostingId.value())))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(new ChangeJobPostingStatusCommand(
                        JobPostingStatus.CLOSED
                ))
                .exchange()
                .expectStatus()
                .isOk();

        // then
        var posting = jobPostingRepository
                .findById(organization.id(), jobPostingId)
                .orElseThrow();

        assertThat(posting.status())
                .isEqualTo(JobPostingStatus.CLOSED);
    }

    @Test
    void shouldRejectJobPostingWhenJobDescriptionBelongsToAnotherOrganization() {

        // given
        var testA = scenario.create();
        var testB = scenario.create();

        // when
        createJobPosting(testA.recruiter(), testA.jobDescriptionId());

        var userA  = testA.recruiter();

        var command = getCommand(testB.jobDescriptionId(), userA);

        var token = authenticationClient.login(userA);

        // when / then
        restTestClient
                .post()
                .uri(url("/api/recruitment/job-posting"))
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

    private static @NonNull CreateJobPostingCommand getCommand(UUID jobDescriptionFromAnotherOrganizationId, TestUser userA) {

       return new CreateJobPostingCommand(
                jobDescriptionFromAnotherOrganizationId,
                userA.id(),
                "Java Developer",
                "Summary",
                "Description",
                java.util.List.of("Development"),
                java.util.List.of("Java"),
                java.util.List.of("Spring"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000"),
                new BigDecimal("18000"),
                "PLN"
        );
    }
}

