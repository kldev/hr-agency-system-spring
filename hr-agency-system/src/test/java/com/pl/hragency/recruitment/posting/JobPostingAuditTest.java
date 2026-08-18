package com.pl.hragency.recruitment.posting;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class JobPostingAuditTest extends BaseRestIntegrationTest {

    @Autowired
    private TestJobApplicationScenario scenario;

    @Autowired
    private TestJobPostingFactory jobPostingFactory;

    @Test
    void shouldCreateAuditEntryWhenJobPostingIsCreated() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        var user =  test.recruiter();
        var companyId = test.companyId();
        var jobDescriptionId =  test.jobDescriptionId();

        // when
        var jobPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                user.id()
        );

        // then
        assertThat(jobPostingId)
                .isNotNull();

        var auditEntries = awaitAuditEntries(
                "JobPosting",
                jobPostingId,
                1
        );

        assertThat(auditEntries)
                .hasSize(1);

        assertThat(auditEntries)
                .singleElement()
                .satisfies(entry -> {
                    assertThat(entry.getModule())
                            .isEqualTo("recruitment");

                    assertThat(entry.getAggregateType())
                            .isEqualTo("JobPosting");

                    assertThat(entry.getAggregateId())
                            .isEqualTo(jobPostingId);

                    assertThat(entry.getActorId())
                            .isEqualTo(user.id());
                });
    }

    @Test
    void shouldCreateAuditEntryWhenJobPostingStatusIsChanged() {
        // given
        var test = scenario.create();
        var organization = test.organization();

        var user =  test.recruiter();
        var companyId = test.companyId();
        var jobDescriptionId =  test.jobDescriptionId();

        var jobPostingId = jobPostingFactory.create(
                organization.id(),
                jobDescriptionId,
                companyId,
                user.id()
        );

        jobPostingFactory.updateStatus(
                organization.id(),
                user.id(),
                jobPostingId,
                JobPostingStatus.PUBLISHED
        );

        // then
        var auditEntries = awaitAuditEntries(
                "JobPosting",
                jobPostingId,
                2
        );

        assertThat(auditEntries)
                .hasSize(2);

        assertThat(auditEntries)
                .anySatisfy(entry -> {
                    assertThat(entry.getModule())
                            .isEqualTo("recruitment");

                    assertThat(entry.getAggregateType())
                            .isEqualTo("JobPosting");

                    assertThat(entry.getAggregateId())
                            .isEqualTo(jobPostingId);

                    assertThat(entry.getActorId())
                            .isEqualTo(user.id());

                    assertThat(entry.getEventType())
                            .isEqualTo(AuditEventType.STATUS_CHANGED);
                });
    }
}