package com.pl.hragency.recruitment.application;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.recruitment.application.query.JobApplicationNoteItem;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobApplicationNoteQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobApplicationScenario scenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private TestJobApplicationNoteFactory  jobApplicationNoteFactory;

    @Test
    void shouldReturnNotesForJobApplication() {
        // given
        var test = scenario.create();

        var firstContent = "Strong candidate. Proceed with the offer.";
        var secondContent = "Candidate confirmed availability.";

        var firstNoteId = jobApplicationNoteFactory.create(
                test.organization().id(),
                test.recruiter().id(),
                test.jobApplicationId(),
                firstContent
        );

        var secondNoteId = jobApplicationNoteFactory.create(
                test.organization().id(),
                test.recruiter().id(),
                test.jobApplicationId(),
                secondContent
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var notes = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(test.jobApplicationId())
                ))
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<List<JobApplicationNoteItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(notes)
                .hasSize(2)
                .extracting(JobApplicationNoteItem::id)
                .containsExactlyInAnyOrder(
                        firstNoteId,
                        secondNoteId
                );

        assertThat(notes)
                .extracting(JobApplicationNoteItem::content)
                .containsExactlyInAnyOrder(
                        firstContent,
                        secondContent
                );
    }

    @Test
    void shouldReturnEmptyListWhenJobApplicationHasNoNotes() {
        // given
        var test = scenario.create();


        var token = authenticationClient.login(test.recruiter());

        // when
        var notes = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(test.jobApplicationId())
                ))
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<List<JobApplicationNoteItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(notes).isEmpty();
    }

    @Test
    void shouldReturnOnlyNotesForRequestedJobApplication() {
        // given
        var test = scenario.create();


        var firstApplicationId = scenario.createApplication(test);
        var secondApplicationId  = scenario.createApplication(test);

        var firstNoteId = jobApplicationNoteFactory.create(
                test.organization().id(),
                test.recruiter().id(),
                firstApplicationId.applicationId(),
                "Note for first application"
        );

        jobApplicationNoteFactory.create(
                test.organization().id(),
                test.recruiter().id(),
                secondApplicationId.applicationId(),
                "Note for second application"
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var notes = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(firstApplicationId)
                ))
                .header("Authorization", "Bearer " + token)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(
                        new ParameterizedTypeReference<List<JobApplicationNoteItem>>() {}
                )
                .returnResult()
                .getResponseBody();

        // then
        assertThat(notes)
                .hasSize(1)
                .first()
                .satisfies(note -> {
                    assertThat(note.id()).isEqualTo(firstNoteId);
                    assertThat(note.content())
                            .isEqualTo("Note for first application");
                    assertThat(note.authorName()).isNotEmpty();
                    assertThat(note.createdAt()).isNotNull();
                });
    }
}