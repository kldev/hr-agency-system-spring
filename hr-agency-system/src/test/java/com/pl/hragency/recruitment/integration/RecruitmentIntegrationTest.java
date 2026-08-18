package com.pl.hragency.recruitment.integration;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.application.command.CreateIntegrationClientCommand;
import com.pl.hragency.identity.application.result.IntegrationClientResult;
import com.pl.hragency.identity.domain.model.IntegrationScope;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.application.result.ApplyForPostingResult;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;

public class RecruitmentIntegrationTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobApplicationScenario scenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;


    private IntegrationClientResult createIntegrationClient(
            TestUser user
    )
    {
        var command = new CreateIntegrationClientCommand("Test-SK", Set.of(IntegrationScope.APPLICATION_WRITE));
        var token = authenticationClient.login(user);
        return restTestClient
                .post()
                .uri(url("/api/integration-clients"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(IntegrationClientResult.class)
                .returnResult()
                .getResponseBody();
    }

    private ApplyForPostingResult applyForJobPosting(String apiKey, CreateJobApplicationCommand command){

        return restTestClient
                .post()
                .uri(url("/api/integrations/job-applications"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", apiKey)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ApplyForPostingResult.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    public void shouldApplyForJobPosting(){
        // given
        var test = scenario.create();
        var jobPostingId = test.jobPostingId();
        var key = createIntegrationClient(test.admin());

        var command =  new CreateJobApplicationCommand(jobPostingId, "test@gmail.com", "Test", "User","", CandidateSource.FACEBOOK);
        var result = applyForJobPosting(key.apiKey(), command);

        Assertions.assertNotNull(result);
    }

    @Test
    public void shouldNotApplyForJobPostingWhenAnotherOrganization(){
        // given
        var test = scenario.create();
        var test2 = scenario.create();

        var userB = test2.admin();
        var jobPostingId = test.jobPostingId();

        var key = createIntegrationClient(userB);

        var command =  new CreateJobApplicationCommand(jobPostingId, "test@gmail.com", "Test", "User","", CandidateSource.OLX);

        restTestClient
                .post()
                .uri(url("/api/integrations/job-applications"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", key.apiKey())
                .body(command)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
