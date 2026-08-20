package com.pl.hragency.recruitment.candidate;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.shared.rest.SliceResponse;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCandidateScenario;
import com.pl.hragency.testsupport.TestJobApplicationScenario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;


import static org.assertj.core.api.Assertions.assertThat;

class CandidateQueryTest extends BaseRestIntegrationTest {

    @Autowired
    private TestCandidateScenario scenario;

    @Autowired
    private TestJobApplicationScenario jobApplicationScenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldReturnCandidates() {
        // given
        var test = scenario.create("jan.kowalski@example.com");

        var candidate2 = scenario.createCandidate(
                test.organization(),
                test.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com",
                null
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url("/api/recruitment/candidates"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SliceResponse<CandidateItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting(CandidateItem::id)
                .containsExactly(test.id(), candidate2.id());
    }

    @Test
    void shouldFilterCandidatesBySearch() {
        // given
        var test = scenario.create("jan.kowalski@example.com");

        var notMatching = scenario.createCandidate(
                test.organization(),
                test.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com",
                null
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/candidates"
                                + "?search=kowalski"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SliceResponse<CandidateItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting(CandidateItem::id)
                .containsExactly(test.id());

        assertThat(response.content())
                .extracting(CandidateItem::id)
                .doesNotContain(notMatching.id());
    }

    @Test
    void shouldReturnEmptyResultWhenSearchDoesNotMatch() {
        // given
        var test = scenario.create("jan.kowalski@example.com");

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/candidates"
                                + "?search=does-not-exist"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SliceResponse<CandidateItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content()).isEmpty();
        assertThat(response.hasNext()).isFalse();
    }

    @Test
    void shouldSearchByEmail() {
        // given
        var test = scenario.create("jan.kowalski@example.com");

        scenario.createCandidate(
                test.organization(),
                test.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com",
                null
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/candidates"
                                + "?search=jan.kowalski"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SliceResponse<CandidateItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting(CandidateItem::id)
                .containsExactly(test.id());
    }

    @Test
    void shouldReturnCandidatesUsingPagination() {
        // given
        var test = scenario.create("adam.nowak@example.com");

        scenario.createCandidate(
                test.organization(),
                test.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak2@example.com",
                null
        );

        scenario.createCandidate(
                test.organization(),
                test.recruiter(),
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com",
                null
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/candidates"
                                + "?page=0"
                                + "&size=1"
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SliceResponse<CandidateItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content()).hasSize(1);
        assertThat(response.hasNext()).isTrue();
    }

    @Test
    void shouldSearchByCompanyId() {
        // given
        var test = jobApplicationScenario.create();

        var secondApplicationSameCompany = jobApplicationScenario.createApplication(test);

        scenario.createCandidate(
                test.organization(),
                test.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com",
                null
        );

        var token = authenticationClient.login(test.recruiter());

        // when
        var response = restTestClient.get()
                .uri(url(
                        "/api/recruitment/candidates"
                                + "?companyId=%s".formatted(test.companyId())
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<SliceResponse<CandidateItem>>() {
                })
                .returnResult()
                .getResponseBody();

        // then
        assertThat(response).isNotNull();
        assertThat(response.content())
                .extracting(CandidateItem::id)
                .containsExactly(test.candidateId(), secondApplicationSameCompany.candidateId());
    }
}