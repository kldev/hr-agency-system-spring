package com.pl.hragency.jobdescription;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.audit.adapter.persistence.AuditJpaEntity;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.application.command.ChangeJobDescriptionStatusCommand;
import com.pl.hragency.jobdescription.domain.model.JobDescriptionStatus;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class JobDescriptionAuditTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobDescriptionScenario scenario;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldCreateAuditEntryWhenJobDescriptionIsCreated() {
        // given
        var test = scenario.create();
        var jobDescriptionId = test.jobDescriptionId();

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
                            .isEqualTo(test.recruiter().id());
                });
    }

    @Test
    void shouldCreateAuditEntryWhenJobDescriptionStatusIsChanged() {
        // given
        var test = scenario.create();
        var jobDescriptionId = test.jobDescriptionId();

        var command = new ChangeJobDescriptionStatusCommand(
                JobDescriptionStatus.OPEN
        );

        var token = authenticationClient.login(test.recruiter());

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
                            .isEqualTo(test.recruiter().id());

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