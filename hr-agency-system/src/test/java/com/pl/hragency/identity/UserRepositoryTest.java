package com.pl.hragency.identity;


import com.pl.hragency.BaseIntegrationTest;
import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestOrganizationFactory testOrganizationFactory;


    @Test
    void shouldSaveAndFindUserByEmailAndOrganizationId() {

        // given
        var organization1 =
                testOrganizationFactory.create();
        var userOrg =   new UserOrganizationId(organization1.id());

        var user = User.create(
                userOrg,
                "john.smith@acme.com",
                "John",
                "Smith",
                OrganizationRole.RECRUITER,
                "bcrypt-hash"
        );

        // when
        userRepository.save(user);

        var result = userRepository.findByEmailAndOrganizationId(
                "john.smith@acme.com",
                userOrg
        );

        // then
        assertThat(result)
                .isPresent();

        var found = result.get();

        assertThat(found.id())
                .isEqualTo(user.id());

        assertThat(found.organizationId())
                .isEqualTo(userOrg);

        assertThat(found.email())
                .isEqualTo("john.smith@acme.com");

        assertThat(found.firstName())
                .isEqualTo("John");

        assertThat(found.lastName())
                .isEqualTo("Smith");

        assertThat(found.role())
                .isEqualTo(OrganizationRole.RECRUITER);

        assertThat(found.passwordHash())
                .isEqualTo("bcrypt-hash");

        assertThat(found.createdAt().truncatedTo(ChronoUnit.SECONDS))
                .isEqualTo(user.createdAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    void shouldNotFindUserWhenEmailBelongsToAnotherOrganization() {

        // given
        var organization1 =
                testOrganizationFactory.create();

        var organization2 =
                testOrganizationFactory.create();

        var user = User.create(
                new UserOrganizationId(organization1.id()),
                "john.smith@acme.com",
                "John",
                "Smith",
                OrganizationRole.RECRUITER,
                "bcrypt-hash"
        );

        userRepository.save(user);

        // when
        var result = userRepository.findByEmailAndOrganizationId(
                "john.smith@acme.com",
                new UserOrganizationId(organization2.id())
        );

        // then
        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldNotFindUnknownUser() {

        // given
        var organizationId =
                new UserOrganizationId(UUID.randomUUID());

        // when
        var result = userRepository.findByEmailAndOrganizationId(
                "unknown@acme.com",
                organizationId
        );

        // then
        assertThat(result)
                .isEmpty();
    }

    @Test
    void shouldFindUserByEmailForDevelopmentAndTests() {

        // given
        var organizationId =
                testOrganizationFactory.create();
        var userOrg =   new UserOrganizationId(organizationId.id());

        var user = User.create(
                userOrg,
                "alex.cross@acme.com",
                "Alex",
                "Cross",
                OrganizationRole.RECRUITER,
                "bcrypt-hash"
        );

        userRepository.save(user);

        // when
        var result = userRepository.findByEmail(
                "alex.cross@acme.com"
        );

        // then
        assertThat(result)
                .isPresent();

        assertThat(result.get().id())
                .isEqualTo(user.id());

        assertThat(result.get().organizationId())
                .isEqualTo(userOrg);
    }

    @Test
    void shouldAllowSameEmailInDifferentOrganizations() {

        // given
        var organization1 =
              testOrganizationFactory.create();

        var organization2 =
                testOrganizationFactory.create();

        var org1 =   new UserOrganizationId(organization1.id());
        var org2 =   new UserOrganizationId(organization2.id());
        var user1 = User.create(
                org1,
                "john.smith@example.com",
                "John",
                "Smith",
                OrganizationRole.RECRUITER,
                "hash1"
        );

        var user2 = User.create(
                org2,
                "john.smith@example.com",
                "John",
                "Smith",
                OrganizationRole.RECRUITER,
                "hash2"
        );

        // when
        userRepository.save(user1);
        userRepository.save(user2);

        // then
        var result1 =
                userRepository.findByEmailAndOrganizationId(
                        "john.smith@example.com",
                        org1
                );

        var result2 =
                userRepository.findByEmailAndOrganizationId(
                        "john.smith@example.com",
                        org2
                );

        assertThat(result1)
                .isPresent();

        assertThat(result2)
                .isPresent();

        assertThat(result1.get().id())
                .isEqualTo(user1.id());

        assertThat(result2.get().id())
                .isEqualTo(user2.id());
    }
}
