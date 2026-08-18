package com.pl.hragency.recruitment.interviews;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.recruitment.application.command.CreateInterviewCommand;
import com.pl.hragency.recruitment.application.query.InterviewItem;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InterviewQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    @Autowired
    private TestJobPostingFactory jobPostingFactory;

    @Autowired
    private TestJobApplicationFactory jobApplicationFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnInterviews() {
        // given
        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var application1 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var application2 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var interview1 = createInterview(
                recruiter,
                application1,
                LocalDateTime.of(2026, 9, 15, 10, 30),
                ZoneId.of("Europe/Warsaw")
        );

        var interview2 = createInterview(
                recruiter,
                application2,
                LocalDateTime.of(2026, 9, 16, 11, 30),
                ZoneId.of("Europe/Warsaw")
        );

        var token = authenticationClient.login(recruiter);

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/interviews"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<InterviewItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting("id")
                .containsExactly(interview1, interview2);
    }

    @Test
    void shouldReturnInterviewsFromDateRange() {
        // given
        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var application1 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var application2 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var application3 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var beforeRange = createInterview(
                recruiter,
                application1,
                LocalDateTime.of(2026, 9, 10, 10, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var insideRange1 = createInterview(
                recruiter,
                application2,
                LocalDateTime.of(2026, 9, 15, 10, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var insideRange2 = createInterview(
                recruiter,
                application3,
                LocalDateTime.of(2026, 9, 20, 10, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var token = authenticationClient.login(recruiter);

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/interviews"
                                + "?from=2026-09-15"
                                + "&to=2026-09-20"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<InterviewItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting("id")
                .containsExactly(
                        insideRange1,
                        insideRange2
                );

        assertThat(response.content())
                .extracting("id")
                .doesNotContain(beforeRange);
    }

    @Test
    void shouldReturnOnlyMyInterviews() {
        // given
        var organization = organizationFactory.create();

        var recruiter1 = userFactory.create(
                organization,
                "recruiter1@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var recruiter2 = userFactory.create(
                organization,
                "recruiter2@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var application1 = createJobApplication(
                organization.id(),
                recruiter1.id()
        );

        var application2 = createJobApplication(
                organization.id(),
                recruiter2.id()
        );

        var myInterview = createInterview(
                recruiter1,
                application1,
                LocalDateTime.of(2026, 9, 15, 10, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var otherInterview = createInterview(
                recruiter2,
                application2,
                LocalDateTime.of(2026, 9, 16, 10, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var token = authenticationClient.login(recruiter1);

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/interviews?onlyMine=true"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<InterviewItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting("id")
                .containsExactly(myInterview);

        assertThat(response.content())
                .extracting("id")
                .doesNotContain(otherInterview);
    }

    @Test
    void shouldReturnInterviewsInScheduledAtOrder() {
        // given
        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var application1 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var application2 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var application3 = createJobApplication(
                organization.id(),
                recruiter.id()
        );

        var lateInterview = createInterview(
                recruiter,
                application1,
                LocalDateTime.of(2026, 9, 20, 15, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var earlyInterview = createInterview(
                recruiter,
                application2,
                LocalDateTime.of(2026, 9, 15, 10, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var middleInterview = createInterview(
                recruiter,
                application3,
                LocalDateTime.of(2026, 9, 17, 12, 0),
                ZoneId.of("Europe/Warsaw")
        );

        var token = authenticationClient.login(recruiter);

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/interviews"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<PageResponse<InterviewItem>>() {})
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting("id")
                .containsExactly(
                        earlyInterview,
                        middleInterview,
                        lateInterview
                );
    }

    private UUID createInterview(
            TestUser recruiter,
            UUID applicationId,
            LocalDateTime scheduledAt,
            ZoneId timezone
    ) {
        var command = new CreateInterviewCommand(
                scheduledAt,
                timezone
        );

        var token = authenticationClient.login(recruiter);

        return restTestClient.post()
                .uri(url(
                        "/api/recruitment/job-applications/%s/schedule-interview"
                                .formatted(applicationId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UUID.class)
                .returnResult()
                .getResponseBody();
    }

    private UUID createJobApplication(
            UUID organizationId,
            UUID recruiterId
    ) {
        var companyId = companyFactory.create(organizationId);

        var jobDescriptionId = jobDescriptionFactory.create(
                organizationId,
                companyId,
                recruiterId
        );

        var jobPostingId = jobPostingFactory.create(
                organizationId,
                jobDescriptionId,
                companyId,
                recruiterId
        );

        jobPostingFactory.updateStatus(
                organizationId,
                recruiterId,
                jobPostingId,
                JobPostingStatus.PUBLISHED
        );

        return jobApplicationFactory.create(
                organizationId,
                recruiterId,
                jobPostingId
        );
    }
}