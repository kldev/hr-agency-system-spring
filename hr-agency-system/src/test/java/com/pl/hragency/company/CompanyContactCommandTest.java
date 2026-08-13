package com.pl.hragency.company;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.company.application.command.CreateCompanyContactCommand;
import com.pl.hragency.company.domain.model.CompanyContactId;
import com.pl.hragency.company.application.port.CompanyContactRepository;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class CompanyContactCommandTest extends BaseRestIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private CompanyContactRepository companyContactRepository;

    @Test
    void shouldCreateCompanyContact() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateCompanyContactCommand(
                "john.smith@test.com",
                "+48 600 100 200",
                "John",
                "Smith",
                "HR Manager",
                false
        );

        // when
        var contactId = restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/contact"
                                .formatted(companyId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyContactId.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(contactId)
                .isNotNull();

        var contact = companyContactRepository
                .findById(contactId)
                .orElse(null);

        assertThat(contact)
                .isNotNull();

        assertThat(Objects.requireNonNull(contact).companyId().value())
                .isEqualTo(companyId);

        assertThat(contact.organizationId().value())
                .isEqualTo(organization.id());

        assertThat(contact.firstName().value())
                .isEqualTo("John");

        assertThat(contact.lastName().value())
                .isEqualTo("Smith");

        assertThat(contact.email().value())
                .isEqualTo("john.smith@test.com");

        assertThat(contact.phone().value())
                .isEqualTo("+48 600 100 200");

        assertThat(contact.jobTitle().value())
                .isEqualTo("HR Manager");

        assertThat(contact.primaryContact())
                .isFalse();
    }

    @Test
    void shouldCreatePrimaryCompanyContact() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateCompanyContactCommand(
                "primary@test.com",
                "+48 600 123 456",
                "Anna",
                "Kowalska",
                "CEO",
                true
        );

        // when
        var contactId = restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/contact"
                                .formatted(companyId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CompanyContactId.class)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(contactId)
                .isNotNull();

        var contact = companyContactRepository
                .findById(contactId)
                .orElseThrow();

        assertThat(contact.primaryContact())
                .isTrue();

        assertThat(contact.firstName().value())
                .isEqualTo("Anna");

        assertThat(contact.lastName().value())
                .isEqualTo("Kowalska");
    }

    @Test
    void shouldNotCreateContactForCompanyFromAnotherOrganization() {

        // given
        var organization1 = organizationFactory.create();
        var organization2 = organizationFactory.create();

        var user = userFactory.create(
                organization1,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization2.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateCompanyContactCommand(
                "john@test.com",
                "+48 600 111 222",
                "John",
                "Smith",
                "Manager",
                false
        );

        // when / then
        restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/contact"
                                .formatted(companyId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void shouldReturnBadRequestWhenContactDataIsInvalid() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var token = authenticationClient.login(user);

        var command = new CreateCompanyContactCommand(
                "invalid-email",
                "",
                "",
                "",
                "",
                false
        );

        // when / then
        restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/contact"
                                .formatted(companyId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    void shouldReturnNotFoundWhenCompanyDoesNotExist() {

        // given
        var organization = organizationFactory.create();

        var user = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        var token = authenticationClient.login(user);

        var companyId = java.util.UUID.randomUUID();

        var command = new CreateCompanyContactCommand(
                "john@test.com",
                "+48 600 111 222",
                "John",
                "Smith",
                "Manager",
                false
        );

        // when / then
        restTestClient
                .post()
                .uri(url(
                        "/api/company/%s/contact"
                                .formatted(companyId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header(
                        "Authorization",
                        "Bearer " + token
                )
                .body(command)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}

