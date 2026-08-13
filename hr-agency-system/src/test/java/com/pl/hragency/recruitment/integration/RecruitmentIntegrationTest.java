package com.pl.hragency.recruitment.integration;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.application.command.CreateIntegrationClientCommand;
import com.pl.hragency.identity.application.result.IntegrationClientResult;
import com.pl.hragency.identity.domain.model.IntegrationScope;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.command.CreateJobPostingCommand;
import com.pl.hragency.recruitment.application.command.UpdateCandidateCommand;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Set;

public class RecruitmentIntegrationTest extends BaseRestIntegrationTest {
    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    private JobPostingId createJobPosting(
            TestOrganization organization,
            TestUser user
    ) {
        var companyId = companyFactory.create(organization.id());

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

        var command = new CreateJobPostingCommand(
                jobDescriptionId,
                user.id(),
                "Java Developer",
                "Java Developer for recruitment project",
                "We are looking for an experienced Java Developer.",
                java.util.List.of("Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with frontend developers"),
                java.util.List.of("3+ years of Java experience",
                        "Spring Boot experience",
                        "Good English"),
                java.util.List.of(   "Java",
                        "Spring Boot",
                        "PostgreSQL",
                        "Docker"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new BigDecimal("12000.00"),
                new BigDecimal("18000.00"),
                "PLN"
        );

        var token = authenticationClient.login(user);

        return restTestClient
                .post()
                .uri(url("/api/recruitment/job-posting"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(JobPostingId.class)
                .returnResult()
                .getResponseBody();
    }

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

    private ApiResult applyForJobPosting(String apiKey, CreateJobApplicationCommand command){

        return restTestClient
                .post()
                .uri(url("/api/integrations/job-applications"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-API-KEY", apiKey)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ApiResult.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    public void shouldApplyForJobPosting(){
        // given
        var organization = organizationFactory.create();
        var user = userFactory.create(organization, "admin@hr-app.com", "pass123", OrganizationRole.ADMIN);
        var jobPostingId = createJobPosting(organization, user);
        var key = createIntegrationClient(user);

        var result = applyForJobPosting(key.apiKey(), new CreateJobApplicationCommand(jobPostingId.value(), "test@gmail.com", "Test", "User","", CandidateSource.FACEBOOK));

        Assertions.assertNotNull(result);
    }

    @Test
    public void shouldNotApplyForJobPostingWhenAnotherOrganization(){
        // given
        var organizationA = organizationFactory.create();
        var organizationB = organizationFactory.create();
        var userA = userFactory.create(organizationA, "admin@org-one.io", "pass123", OrganizationRole.ADMIN);
        var userB = userFactory.create(organizationB, "admin@org-two.io", "pass123", OrganizationRole.ADMIN);
        var jobPostingId = createJobPosting(organizationA, userA);
        var key = createIntegrationClient(userB);

        var command =  new CreateJobApplicationCommand(jobPostingId.value(), "test@gmail.com", "Test", "User","", CandidateSource.OLX);

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
