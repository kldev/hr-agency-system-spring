package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.BaseIntegrationTest;
import com.pl.hragency.recruitment.application.port.JobApplicationQueryRepository;
import com.pl.hragency.recruitment.application.query.JobApplicationListQuery;
import com.pl.hragency.testsupport.TestJobApplicationScenario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

public class JobApplicationQueryRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private TestJobApplicationScenario jobApplicationScenario;

    @Autowired
    private JobApplicationQueryRepository jobApplicationQueryRepository;

    @Test
    public void shouldReturnAllApplicationsWhenNoFiltersAreSpecified() {
        var test = jobApplicationScenario.create();

        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var query = JobApplicationListQuery.empty();

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response).isNotNull();
        assertThat(response.content()).hasSize(3);
    }

    @Test
    public void shouldFilterApplicationsByCompanyId() {
        var test = jobApplicationScenario.create();

        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var other = jobApplicationScenario.create();
        jobApplicationScenario.createApplication(other);

        var query = new JobApplicationListQuery(
                null,
                test.companyId(),
                null,
                null,
                null,
                null
        );

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response.content()).hasSize(3);
        assertThat(response.content())
                .allMatch(application -> application.companyId().equals(test.companyId()));
    }

    @Test
    public void shouldReturnEmptyResultWhenCompanyHasNoApplications() {
        var test = jobApplicationScenario.create();
        jobApplicationScenario.createApplication(test);

        var other = jobApplicationScenario.create();

        var query = new JobApplicationListQuery(
                null,
                other.companyId(),
                null,
                null,
                null,
                null
        );

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response.content()).isEmpty();
    }

    @Test
    public void shouldFilterApplicationsByRecruiterId() {
        var test = jobApplicationScenario.create();
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var other = jobApplicationScenario.create();

        var query = new JobApplicationListQuery(
                null,
                null,
                null,
                null,
                test.recruiter().id(),
                null
        );

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response.content()).hasSize(3);
        assertThat(response.content())
                .allMatch(application ->
                        application.recruiterId().equals(test.recruiter().id()));
    }

    @Test
    public void shouldReturnEmptyResultWhenRecruiterHasNoApplications() {
        var test = jobApplicationScenario.create();
        jobApplicationScenario.createApplication(test);

        var other = jobApplicationScenario.create();

        var query = new JobApplicationListQuery(
                null,
                null,
              null,
                null,
                other.recruiter().id(),
                null
        );

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response.content()).isEmpty();
    }

    @Test
    public void shouldRespectOrganizationIsolation() {
        var test = jobApplicationScenario.create();
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var otherOrganization = jobApplicationScenario.create();
        jobApplicationScenario.createApplication(otherOrganization);
        jobApplicationScenario.createApplication(otherOrganization);

        var query = JobApplicationListQuery.empty();

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response.content()).hasSize(3);
    }

    @Test
    public void shouldApplyCompanyAndRecruiterFiltersTogether() {
        var test = jobApplicationScenario.create();

        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var query = new JobApplicationListQuery(
                null,
                test.companyId(),
                null,
                null,
                test.recruiter().id(),
                null
        );

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 20)
        );

        assertThat(response.content()).hasSize(3);

        assertThat(response.content())
                .allMatch(application ->
                        application.companyId().equals(test.companyId())
                                && application.recruiterId().equals(test.recruiter().id()));
    }

    @Test
    public void shouldRespectPageSize() {
        var test = jobApplicationScenario.create();

        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var query = JobApplicationListQuery.empty();

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(0, 2)
        );

        assertThat(response.content()).hasSize(2);

    }

    @Test
    public void shouldReturnSecondPage() {
        var test = jobApplicationScenario.create();

        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);
        jobApplicationScenario.createApplication(test);

        var query = JobApplicationListQuery.empty();

        var response = jobApplicationQueryRepository.search(
                test.organizationId(),
                query,
                PageRequest.of(1, 2)
        );

        assertThat(response.content()).hasSize(2);

    }
}