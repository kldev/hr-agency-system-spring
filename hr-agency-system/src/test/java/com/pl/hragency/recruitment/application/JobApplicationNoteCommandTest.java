package com.pl.hragency.recruitment.application;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationNoteCommand;
import com.pl.hragency.recruitment.application.port.JobApplicationNoteRepository;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestJobApplicationFactory;
import com.pl.hragency.testsupport.TestJobDescriptionFactory;
import com.pl.hragency.testsupport.TestJobPostingFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JobApplicationNoteCommandTest extends BaseRestIntegrationTest {

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

    @Autowired
    private JobApplicationNoteRepository jobApplicationNoteRepository;

    @Test
    void shouldCreateJobApplicationNote() {
        // given
        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var companyId = companyFactory.create(organization.id());

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                recruiter.id()
        );

        var jobPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                recruiter.id()
        );

        jobPostingFactory.updateStatus(organization.id(), recruiter.id(), jobPostingId, JobPostingStatus.PUBLISHED);

        var jobApplicationId = jobApplicationFactory.create(
                organization.id(),
                recruiter.id(),
                jobPostingId
        );

        var content = "Strong candidate. Proceed with the offer.";
        var command = new CreateJobApplicationNoteCommand(content);

        var token = authenticationClient.login(recruiter);

        // when
        var noteId = restTestClient.post()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(jobApplicationId)
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
                organization.id(),
                jobApplicationId
        );

        assertThat(notes)
                .hasSize(1)
                .first()
                .satisfies(note -> {
                    assertThat(note.id()).isEqualTo(noteId);
                    assertThat(note.applicationId()).isEqualTo(jobApplicationId);
                    assertThat(note.content()).isEqualTo(content);
                });
    }
}