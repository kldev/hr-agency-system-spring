package com.pl.hragency.recruitment.candidate;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.recruitment.adapter.rest.candidate.model.CandidateResponse;
import com.pl.hragency.recruitment.application.command.CreateCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateEmail;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.shared.rest.ApiValidationError;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationScenario;
import com.pl.hragency.testsupport.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class CandidateCreateTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationScenario organizationScenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private CandidateRepository repository;

    private CandidateResponse createCandidate(
            TestUser user,
            CreateCandidateCommand command
    ) {
        var token = authenticationClient.login(user);

        return restTestClient.post()
                .uri(url("/api/recruitment/candidates"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CandidateResponse.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void shouldCreateCandidate() {
        var scenario = organizationScenario.create();

        var command = new CreateCandidateCommand(
                "new-candidate@mail.com",
                "",
                "",
                "",
                CandidateSource.CAREER_PAGE
        );

        var response = createCandidate(
                scenario.recruiter(),
                command
        );

        assertThat(response).isNotNull();
        assertThat(response.email())
                .isEqualTo("new-candidate@mail.com");
    }

    @Test
    void shouldCreateWithNameAndLastnameCandidate() {
        var scenario = organizationScenario.create();

        var command = new CreateCandidateCommand(
                "new-candidate2@mail.com",
                "John",
                "Smith",
                "+4812300000",
                CandidateSource.CAREER_PAGE
        );

        var response = createCandidate(
                scenario.recruiter(),
                command
        );

        assertThat(response).isNotNull();
        assertThat(response.email())
                .isEqualTo("new-candidate2@mail.com");

        var candidate = repository
                .findByEmail(
                        new CandidateEmail("new-candidate2@mail.com"),
                        scenario.organization().id()
                )
                .orElseThrow();

        assertThat(candidate.email())
                .isEqualTo("new-candidate2@mail.com");
        assertThat(candidate.firstName())
                .isEqualTo("John");
        assertThat(candidate.lastName())
                .isEqualTo("Smith");
        assertThat(candidate.phone())
                .isEqualTo("+4812300000");
    }

    @Test
    void shouldReturnValidationResultWhenNoEmail() {
        var scenario = organizationScenario.create();

        var command = new CreateCandidateCommand(
                "",
                "John",
                "Smith",
                "+4812300000",
                CandidateSource.CAREER_PAGE
        );

        var token = authenticationClient.login(scenario.recruiter());

        var validationResult = restTestClient.post()
                .uri(url("/api/recruitment/candidates"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isBadRequest()
                .expectBody(ApiValidationError.class)
                .returnResult();

        assertThat(validationResult).isNotNull();
    }
}
