package com.pl.hragency.recruitment.application;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.recruitment.application.query.JobApplicationNoteItem;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestJobApplicationFactory;
import com.pl.hragency.testsupport.TestJobApplicationNoteFactory;
import com.pl.hragency.testsupport.TestJobDescriptionFactory;
import com.pl.hragency.testsupport.TestJobPostingFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobApplicationNoteQueryTest extends BaseRestIntegrationTest {

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
    private TestJobApplicationNoteFactory jobApplicationNoteFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnNotesForJobApplication() {
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

        var firstContent = "Strong candidate. Proceed with the offer.";
        var secondContent = "Candidate confirmed availability.";

        var firstNoteId = jobApplicationNoteFactory.create(
                organization.id(),
                recruiter.id(),
                jobApplicationId,
                firstContent
        );

        var secondNoteId = jobApplicationNoteFactory.create(
                organization.id(),
                recruiter.id(),
                jobApplicationId,
                secondContent
        );

        var token = authenticationClient.login(recruiter);

        // when
        var notes = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(jobApplicationId)
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

        var token = authenticationClient.login(recruiter);

        // when
        var notes = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications/%s/notes"
                                .formatted(jobApplicationId)
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

        var firstApplicationId = jobApplicationFactory.create(
                organization.id(),
                recruiter.id(),
                jobPostingId
        );

        var secondApplicationId = jobApplicationFactory.create(
                organization.id(),
                recruiter.id(),
                jobPostingId
        );

        var firstNoteId = jobApplicationNoteFactory.create(
                organization.id(),
                recruiter.id(),
                firstApplicationId,
                "Note for first application"
        );

        jobApplicationNoteFactory.create(
                organization.id(),
                recruiter.id(),
                secondApplicationId,
                "Note for second application"
        );

        var token = authenticationClient.login(recruiter);

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