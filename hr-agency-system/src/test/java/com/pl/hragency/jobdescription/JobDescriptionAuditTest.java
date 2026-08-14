package com.pl.hragency.jobdescription;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.audit.adapter.persistence.AuditJpaEntity;
import com.pl.hragency.audit.domain.model.AuditEntry;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.jobdescription.application.command.ChangeJobDescriptionStatusCommand;
import com.pl.hragency.jobdescription.application.command.CreateJobDescriptionCommand;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JobDescriptionAuditTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    private UUID createJobDescription(UUID organizationId, UUID companyId, TestUser user)
    {
        var command = new CreateJobDescriptionCommand(
                companyId,
                "Senior Java Developer",
                "Experienced Java developer for backend development.",
                "We are looking for an experienced Java developer to join our team.",
                List.of(
                        "Develop backend applications",
                        "Review code"
                ),
                List.of(
                        "At least 5 years of Java experience",
                        "Experience with Spring Boot"
                ),
                List.of(
                        "Java",
                        "Spring Boot",
                        "PostgreSQL"
                ),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                BigDecimal.valueOf(12000),
                BigDecimal.valueOf(18000),
                "PLN"
        );

        var token = authenticationClient.login(user);

        // when
        return restTestClient
                .post()
                .uri(url("/api/job-description"))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(UUID.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void shouldCreateAuditEntryWhenJobDescriptionIsCreated() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var jobDescriptionId = createJobDescription(organization.id(), companyId, user);

        // then
        assertThat(jobDescriptionId)
                .isNotNull();

        var auditEntries = awaitAuditEntries(
                "JobDescription",
                jobDescriptionId,
                1
        );

        assertThat(auditEntries)
                .hasSize(1);

        assertThat(auditEntries)
                .singleElement()
                .satisfies(entry -> {
                    assertThat(entry.getModule())
                            .isEqualTo("job-description");
                    assertThat(entry.getAggregateType())
                            .isEqualTo("JobDescription");

                    assertThat(entry.getAggregateId())
                            .isEqualTo(jobDescriptionId);

                    assertThat(entry.getActorId())
                            .isEqualTo(user.id());
                });
    }

    @Test
    void shouldCreateAuditEntryWhenJobDescriptionStatusIsChanged() {
        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var jobDescriptionId = createJobDescription(
                organization.id(),
                companyId,
                user
        );

        var command = new ChangeJobDescriptionStatusCommand(
                JobDescriptionStatus.OPEN
        );

        var token = authenticationClient.login(user);

        // when
        restTestClient
                .put()
                .uri(url("/api/job-description/%s/status"
                        .formatted(jobDescriptionId)))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isOk();

        // then
        var auditEntries = awaitAuditEntries(
                "JobDescription",
                jobDescriptionId,
                2
        );

        assertThat(auditEntries)
                .hasSize(2);

        assertThat(auditEntries)
                .anySatisfy(entry -> {
                    assertThat(entry.getAggregateType())
                            .isEqualTo("JobDescription");

                    assertThat(entry.getAggregateId())
                            .isEqualTo(jobDescriptionId);

                    assertThat(entry.getActorId())
                            .isEqualTo(user.id());

                    assertThat(entry.getModule())
                            .isEqualTo("job-description");

                    assertThat(entry.getEventType())
                            .isEqualTo(AuditEventType.STATUS_CHANGED);
                });

        assertThat(auditEntries)
                .extracting(AuditJpaEntity::getEventType)
                .contains(
                        AuditEventType.CREATED,
                        AuditEventType.STATUS_CHANGED
                );
    }
}