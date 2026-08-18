package com.pl.hragency.recruitment.application;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationNoteCommand;
import com.pl.hragency.recruitment.application.port.JobApplicationNoteRepository;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
class JobApplicationNoteCommandTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobApplicationScenario jobApplicationScenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private JobApplicationNoteRepository jobApplicationNoteRepository;

    @Test
    void shouldCreateJobApplicationNote() {
        // given
        var scenario = jobApplicationScenario.create();

        var content = "Strong candidate. Proceed with the offer.";
        var command = new CreateJobApplicationNoteCommand(content);

        var token = authenticationClient.login(scenario.recruiter());

        // when
        var noteId = restTestClient.post()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(scenario.jobApplicationId())
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
        assertThat(noteId).isNotNull();

        var notes = jobApplicationNoteRepository.findAll(
                scenario.organization().id(),
                scenario.jobApplicationId()
        );

        assertThat(notes)
                .hasSize(1)
                .first()
                .satisfies(note -> {
                    assertThat(note.id()).isEqualTo(noteId);
                    assertThat(note.applicationId())
                            .isEqualTo(scenario.jobApplicationId());
                    assertThat(note.content())
                            .isEqualTo(content);
                });
    }
}