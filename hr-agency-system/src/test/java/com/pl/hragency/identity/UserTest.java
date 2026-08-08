package com.pl.hragency.identity;


import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserId;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.identity.domain.model.UserRole;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {

    @Test
    void shouldCreateUser() {

        // given
        var organizationId = UserOrganizationId.newId();

        // when
        var user = User.create(
                organizationId,
                "john.smith@acme.com",
                "John",
                "Smith",
                UserRole.RECRUITER,
                "hashed-password"
        );

        // then
        assertThat(user.id()).isNotNull();
        assertThat(user.organizationId())
                .isEqualTo(organizationId);

        assertThat(user.email())
                .isEqualTo("john.smith@acme.com");

        assertThat(user.firstName())
                .isEqualTo("John");

        assertThat(user.lastName())
                .isEqualTo("Smith");

        assertThat(user.role())
                .isEqualTo(UserRole.RECRUITER);

        assertThat(user.passwordHash())
                .isEqualTo("hashed-password");

        assertThat(user.createdAt())
                .isNotNull();
    }

    @Test
    void shouldGenerateNewIdWhenCreatingUser() {

        // given
        var organizationId = UserOrganizationId.newId();


        // when
        var firstUser = User.create(
                organizationId,
                "john@acme.com",
                "John",
                "Smith",
                UserRole.RECRUITER,
                "password"
        );

        var secondUser = User.create(
                organizationId,
                "jane@acme.com",
                "Jane",
                "Smith",
                UserRole.RECRUITER,
                "password"
        );

        // then
        assertThat(firstUser.id())
                .isNotEqualTo(secondUser.id());
    }

    @Test
    void shouldSetCreatedAtWhenCreatingUser() {

        // given
        var organizationId =
                UserOrganizationId.newId();

        var before = Instant.now();

        // when
        var user = User.create(
                organizationId,
                "john@acme.com",
                "John",
                "Smith",
                UserRole.RECRUITER,
                "password"
        );

        var after = Instant.now();

        // then
        assertThat(user.createdAt())
                .isBetween(before, after);
    }

    @Test
    void shouldRehydrateUser() {

        // given
        var userId = UserId.newId();
        var organizationId = UserOrganizationId.newId();

        var createdAt = Instant.parse(
                "2026-01-15T10:30:00Z"
        );

        // when
        var user = User.rehydrate(
                userId,
                organizationId,
                "john.smith@acme.com",
                "John",
                "Smith",
                UserRole.RECRUITER,
                "hashed-password",
                createdAt
        );

        // then
        assertThat(user.id())
                .isEqualTo(userId);

        assertThat(user.organizationId())
                .isEqualTo(organizationId);

        assertThat(user.email())
                .isEqualTo("john.smith@acme.com");

        assertThat(user.firstName())
                .isEqualTo("John");

        assertThat(user.lastName())
                .isEqualTo("Smith");

        assertThat(user.role())
                .isEqualTo(UserRole.RECRUITER);

        assertThat(user.passwordHash())
                .isEqualTo("hashed-password");

        assertThat(user.createdAt())
                .isEqualTo(createdAt);
    }

    @Test
    void shouldPreserveIdAndCreatedAtWhenRehydrating() {

        // given
        var userId = UserId.newId();
        var organizationId = UserOrganizationId.newId();

        var createdAt = Instant.parse(
                "2025-10-01T08:15:00Z"
        );

        // when
        var user = User.rehydrate(
                userId,
                organizationId,
                "user@acme.com",
                "John",
                "Smith",
                UserRole.SALES,
                "hash",
                createdAt
        );

        // then
        assertThat(user.id()).isSameAs(userId);
        assertThat(user.createdAt()).isSameAs(createdAt);
    }
}
