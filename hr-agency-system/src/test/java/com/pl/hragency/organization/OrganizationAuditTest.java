package com.pl.hragency.organization;

import com.pl.hragency.BaseIntegrationTest;

import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.handler.CreateOrganizationHandler;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationAuditTest extends BaseIntegrationTest {

    @Autowired
    private CreateOrganizationHandler handler;


    @Test
    void shouldCreateAuditLogWhenOrganizationIsCreated() {

        // given
        var command = new CreateOrganizationCommand(
                "ACME Sp. z o.o.",
                "acmeOrg"
        );

        // when
        var organizationId = handler.handle( command).id();

        // then
        var auditEntries = awaitAuditEntries("Organization",
                organizationId, 1);


        assertThat(auditEntries)
                .hasSize(1);

        var audit = auditEntries.getFirst();

        assertThat(audit.getAggregateType())
                .isEqualTo("Organization");

        assertThat(audit.getAggregateId())
                .isEqualTo(organizationId);

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.CREATED);

        assertThat(audit.getModule())
                .isEqualTo("organization");

        assertThat(audit.getData().contains("ACME Sp. z o.o."));

        assertThat(audit.getData().contains("acme"));
    }
}
