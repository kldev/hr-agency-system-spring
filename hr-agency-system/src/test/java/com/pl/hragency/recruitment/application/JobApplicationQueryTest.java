package com.pl.hragency.recruitment.application;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.recruitment.application.query.JobApplicationItem;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestJobApplicationScenario;
import com.pl.hragency.testsupport.TestOrganizationScenario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JobApplicationQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobApplicationScenario scenario;

    @Autowired
    private TestOrganizationScenario organizationScenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnJobApplications() {
        // given
        var test = scenario.create();
        var second = scenario.createApplication(test);
        var third = scenario.createApplication(test);

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/job-applications"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response)
                .extracting(JobApplicationItem::id)
                .containsExactly(
                        test.applicationId(),
                        second.applicationId(),
                        third.applicationId()
                );

      //  assertThat(response.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldReturnEmptyResultWhenThereAreNoApplications() {
        // given
        var test = organizationScenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/job-applications"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();

    }

    @Test
    void shouldFilterByCompanyId() {
        // given
        var test = scenario.create();

        var second = scenario.createApplication(test);
        var third = scenario.createApplication(test);

        var other = scenario.create();
        var otherApplication = scenario.createApplication(other);

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?companyId=%s".formatted(test.companyId())
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .containsExactly(
                        test.applicationId(),
                        second.applicationId(),
                        third.applicationId()
                );

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .doesNotContain(otherApplication.applicationId());

     //   assertThat(response.totalElements()).isEqualTo(3);
    }

    @Test
    void shouldFilterByPostingId() {
        // given
        var test = scenario.create();

        scenario.createApplication(test);
        var second = scenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?postingId=%s".formatted(test.jobPostingId())
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .doesNotContain(second.applicationId());
    }

    @Test
    void shouldFilterByRecruiterId() {
        // given
        var test = scenario.create();

        var second = scenario.createApplication(test);
        var third = scenario.createApplication(test);

        var other = scenario.create();
        var otherApplication = scenario.createApplication(other);

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?recruiterId=%s".formatted(test.recruiter().id())
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .containsExactly(
                        test.applicationId(),
                        second.applicationId(),
                        third.applicationId()
                );

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .doesNotContain(otherApplication.applicationId());
    }

    @Test
    void shouldReturnEmptyResultWhenSearchDoesNotMatch() {
        // given
        var test = scenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?search=does-not-exist"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response).isEmpty();
    }

    @Test
    void shouldReturnApplicationsUsingPagination() {
        // given
        var test = scenario.create();

        scenario.createApplication(test);
        scenario.createApplication(test);
        scenario.createApplication(test);
        scenario.createApplication(test);

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?page=0"
                                + "&size=2"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);

        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void shouldReturnSecondPage() {
        // given
        var test = scenario.create();

        scenario.createApplication(test);
        scenario.createApplication(test);
        scenario.createApplication(test);
        scenario.createApplication(test);

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?page=1"
                                + "&size=2"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response).hasSize(2);
//        assertThat(response.totalElements()).isEqualTo(5);
//        assertThat(response.page()).isEqualTo(1);
//        assertThat(response.size()).isEqualTo(2);
    }

    @Test
    void shouldNotReturnApplicationsFromAnotherOrganization() {
        // given
        var test = scenario.create();
        var otherApplication = scenario.createApplication(test);

        var other = scenario.create();
        var applicationFromOtherOrganization =
                scenario.createApplication(other);

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/job-applications"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<JobApplicationItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .containsExactly(
                        test.applicationId(),
                        otherApplication.applicationId()
                );

        assertThat(response)
                .extracting(JobApplicationItem::id)
                .doesNotContain(applicationFromOtherOrganization.applicationId());
    }

    @Test
    void shouldReturnBadRequestForInvalidCompanyId() {
        // given
        var test = scenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when / then
        restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?companyId=invalid-uuid"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldReturnBadRequestForInvalidPostingId() {
        // given
        var test = scenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when / then
        restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?postingId=invalid-uuid"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldReturnBadRequestForInvalidRecruiterId() {
        // given
        var test = scenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when / then
        restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?recruiterId=invalid-uuid"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldReturnBadRequestForInvalidPage() {
        // given
        var test = scenario.create();

        var token = authenticationClient.login(test.recruiter());

        // when / then
        restTestClient.get()
                .uri(url(
                        "/api/recruitment/job-applications"
                                + "?page=invalid"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}