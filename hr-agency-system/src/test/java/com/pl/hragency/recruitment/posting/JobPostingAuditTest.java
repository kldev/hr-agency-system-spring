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
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    @Autowired
    private TestJobPostingFactory jobPostingFactory;

    @Test
    void shouldCreateAuditEntryWhenJobPostingIsCreated() {
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

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

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

        var jobDescriptionId = jobDescriptionFactory.create(
                organization.id(),
                companyId,
                user.id()
        );

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