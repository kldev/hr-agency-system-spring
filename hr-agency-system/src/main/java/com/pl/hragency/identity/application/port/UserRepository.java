package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserOrganizationId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findByEmailAndOrganizationId(String email, UserOrganizationId organizationId);
    // only for development and tests
    Optional<User> findByEmail(String email);

    void save(
            User user);
    Optional<User> findUser(UUID userId, UUID organizationId);
    List<User> findByOrganizationId(UUID organizationId);
}
