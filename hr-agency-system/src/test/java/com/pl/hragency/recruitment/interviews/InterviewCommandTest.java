package com.pl.hragency.recruitment.interviews;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.recruitment.application.command.CreateInterviewCommand;
import com.pl.hragency.recruitment.application.port.InterviewRepository;
import com.pl.hragency.recruitment.domain.model.interview.InterviewId;
import com.pl.hragency.recruitment.domain.model.interview.InterviewStatus;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestJobApplicationScenario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InterviewCommandTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobApplicationScenario scenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private InterviewRepository interviewRepository;

    @Test
    void shouldScheduleInterviewForJobApplication() {
        // given
        var test = scenario.create();
        var scheduledAt = LocalDateTime.of(
                2026,
                9,
                15,
                10,
                30
        );

        var scheduledTimezone = ZoneId.of("Europe/Warsaw");

        var command = new CreateInterviewCommand(
                scheduledAt,
                scheduledTimezone
        );

        var expectedScheduledAt = scheduledAt
                .atZone(scheduledTimezone)
                .toInstant();

        var token = authenticationClient.login(test.recruiter());

        // when
        var interviewId = restTestClient.post()
                .uri(url(
                        "/api/recruitment/job-applications/%s/schedule-interview"
                                .formatted(test.jobApplicationId())
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

        // then
        assertThat(interviewId).isNotNull();

        var interview = interviewRepository
                .findById(test.organization().id(), new InterviewId(interviewId))
                .orElseThrow();

        assertThat(interview.id().value()).isEqualTo(interviewId);
        assertThat(interview.applicationId()).isEqualTo(test.jobApplicationId());
        assertThat(interview.scheduledAt()).isEqualTo(expectedScheduledAt);
        assertThat(interview.status()).isEqualTo(InterviewStatus.PLANNED);
    }

    @Test
    void shouldScheduleInterviewUsingProvidedTimezone() {
        // given
        var test = scenario.create();

        var scheduledAt = LocalDateTime.of(
                2026,
                9,
                15,
                10,
                30
        );

        var scheduledTimezone = ZoneId.of("America/New_York");

        var command = new CreateInterviewCommand(
                scheduledAt,
                scheduledTimezone
        );

        var expectedScheduledAt = scheduledAt
                .atZone(scheduledTimezone)
                .toInstant();

        var token = authenticationClient.login(test.recruiter());

        // when
        var interviewId = restTestClient.post()
                .uri(url(
                        "/api/recruitment/job-applications/%s/schedule-interview"
                                .formatted(test.jobApplicationId())
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

        // then
        assertThat(interviewId).isNotNull();

        var interview = interviewRepository
                .findById(test.organization().id(), new InterviewId(interviewId))
                .orElseThrow();

        assertThat(interview.applicationId())
                .isEqualTo(test.jobApplicationId());

        assertThat(interview.scheduledAt())
                .isEqualTo(expectedScheduledAt);

        assertThat(interview.scheduledAt())
                .isEqualTo(Instant.parse("2026-09-15T14:30:00Z"));
    }
}