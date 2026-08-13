package com.pl.hragency.identity;


import com.pl.hragency.BaseApiIntegrationTest;
import com.pl.hragency.identity.application.query.UserListItem;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.shared.rest.PageResponse;
import com.pl.hragency.testsupport.AuthenticationTestClient;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import static org.assertj.core.api.Assertions.assertThat;

class UserQueryApiTest extends BaseApiIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    private static final ParameterizedTypeReference<PageResponse<UserListItem>> USER_PAGE =
            new ParameterizedTypeReference<>() {
            };

    @Test
    void shouldReturnUsersFromCurrentOrganization() {

        // given
        var organization = organizationFactory.create();

        var loggedUser = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        userFactory.create(
                organization,
                "john@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        userFactory.create(
                organization,
                "anna@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var token = authenticationClient.login(loggedUser);

        // when
        var page = restTestClient
                .get()
                .uri(url("/api/users?page=0&size=20"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(USER_PAGE)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(page).isNotNull();
        assertThat(page.totalElements()).isEqualTo(3);
        assertThat(page.content()).hasSize(3);
    }

    @Test
    void shouldReturnPagedUsers() {

        // given
        var organization = organizationFactory.create();

        var loggedUser = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        userFactory.create(
                organization,
                "john@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        userFactory.create(
                organization,
                "anna@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        userFactory.create(
                organization,
                "mark@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var token = authenticationClient.login(loggedUser);

        // when
        var page = restTestClient
                .get()
                .uri(url("/api/users?page=0&size=2"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(USER_PAGE)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(page).isNotNull();
        assertThat(page.size()).isEqualTo(2);

        assertThat(page.totalElements()).isEqualTo(4);
        assertThat(page.totalPages()).isEqualTo(2);

    }

    @Test
    void shouldFilterUsersByEmail() {

        // given
        var organization = organizationFactory.create();

        var loggedUser = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        userFactory.create(
                organization,
                "john.smith@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        userFactory.create(
                organization,
                "john.wilson@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        userFactory.create(
                organization,
                "anna@test.com",
                "Password123!",
                OrganizationRole.SALES
        );

        var token = authenticationClient.login(loggedUser);

        // when
        var page = restTestClient
                .get()
                .uri(url("/api/users?search=john&page=0&size=20"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(USER_PAGE)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(page).isNotNull();
        assertThat(page.totalElements()).isEqualTo(2);

        assertThat(page.content())
                .extracting(UserListItem::email)
                .containsExactlyInAnyOrder(
                        "john.smith@test.com",
                        "john.wilson@test.com"
                );
    }

    @Test
    void shouldNotReturnUsersFromAnotherOrganization() {

        // given
        var organization = organizationFactory.create();
        var otherOrganization = organizationFactory.create();

        var loggedUser = userFactory.create(
                organization,
                "admin@test.com",
                "Password123!",
                OrganizationRole.ADMIN
        );

        userFactory.create(
                organization,
                "john@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        userFactory.create(
                otherOrganization,
                "other@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var token = authenticationClient.login(loggedUser);

        // when
        var page = restTestClient
                .get()
                .uri(url("/api/users?page=0&size=20"))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(USER_PAGE)
                .returnResult()
                .getResponseBody();

        // then
        assertThat(page).isNotNull();
        assertThat(page.totalElements()).isEqualTo(2);

        assertThat(page.content())
                .extracting(UserListItem::email)
                .doesNotContain("other@test.com");
    }

    @Test
    void shouldRejectUnauthenticatedUser() {

        restTestClient
                .get()
                .uri(url("/api/users?page=0&size=20"))
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
