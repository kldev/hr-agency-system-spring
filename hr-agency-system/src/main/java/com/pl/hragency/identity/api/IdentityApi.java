package com.pl.hragency.identity.api;

import com.pl.hragency.shared.event.UserSnapshot;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IdentityApi {
    CurrentUser getCurrentUser();
    UUID createUser(String email, String firstName, String lastName, String role, UUID organizationId, String password);
    UUID createPlatformUser(String email, String role, String password);
    void requireRole(String role);
    boolean isCurrentUserSales();
    boolean isCurrentUserRecruiter();
    boolean existsInOrganization(
            UUID userId,
            UUID organizationId
    );
    Optional<UserSnapshot> findUser(UUID userId, UUID organizationId);
    List<UserSuggestion> findUserSuggestions(UUID organizationId, String search, Set<String> roles);
};

