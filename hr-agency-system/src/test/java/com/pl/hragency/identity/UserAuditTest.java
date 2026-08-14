package com.pl.hragency.identity;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.audit.domain.model.AuditEventType;
import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.domain.model.UserId;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class UserAuditTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Test
    void shouldCreateAuditLogWhenOrganizationIsCreated() {
        // given
        var organization =
                organizationFactory.create();

        var user =
                userFactory.create(organization);

        // when
        var token =
                authenticationClient.login(user);

        var command = new CreateUserCommand("test-user-1@fake.mail",
                "some-password",
                "Test", "User", OrganizationRole.SALES);

        var response = restTestClient
                .post()
                .uri(url("/api/users"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(jsonMapper.writeValueAsString(command))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UUID.class)
                .returnResult();

        var userId = response.getResponseBody();

        assertThat(userId).isNotNull();


        var auditEntries = awaitAuditEntries("User", userId,1);

        assertThat(auditEntries)
                .hasSize(1);

        var audit = auditEntries.getFirst();

        assertThat(audit.getAggregateType())
                .isEqualTo("User");

        assertThat(audit.getAggregateId())
                .isEqualTo(userId);

        assertThat(audit.getEventType())
                .isEqualTo(AuditEventType.CREATED);

        assertThat(audit.getModule())
                .isEqualTo("identity");

        assertThat(audit.getData().contains("test-user-1@fake.mail"));

    }
}
