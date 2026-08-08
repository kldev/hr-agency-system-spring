package com.pl.hragency.identity.application.service;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.api.UserSuggestion;
import com.pl.hragency.identity.application.port.AuthorizationService;
import com.pl.hragency.identity.application.port.CurrentUserProvider;
import com.pl.hragency.identity.application.port.PasswordHasher;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.application.query.UserSuggestionsQuery;
import com.pl.hragency.identity.domain.model.User;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.identity.domain.model.UserRole;
import com.pl.hragency.shared.event.UserSnapshot;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IdentityService implements IdentityApi {
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final PasswordHasher hasher;
    private final AuthorizationService authorizationService;
    private final UserSuggestionsQuery userSuggestionsQuery;

    public IdentityService(UserRepository userRepository,
                           CurrentUserProvider currentUserProvider,
                           PasswordHasher hasher,
                           AuthorizationService authorizationService,
                           UserSuggestionsQuery userSuggestionsQuery) {
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.hasher = hasher;
        this.authorizationService = authorizationService;
        this.userSuggestionsQuery = userSuggestionsQuery;
    }

    @Override
    public CurrentUser getCurrentUser() {
        return currentUserProvider.get();
    }

    @Override
    public UUID createUser(String email, String firstName, String lastName, String role, UUID organizationId, String password) {
        User user = User.create(
                new UserOrganizationId(organizationId),
                email,
                firstName,
                lastName,
                UserRole.from(role),
                hasher.hash(password));

        userRepository.save(user);
        return user.id().value();
    }

    @Override
    public void requireRole(String role) {
        authorizationService.requireRole(UserRole.from(role));
    }

    @Override
    public boolean isCurrentUserSales() {
        var user = getCurrentUser();

        return user.role() == UserRole.SALES;
    }

    @Override
    public boolean existsInOrganization(UUID userId, UUID organizationId) {
        return userRepository.existsInOrganization(userId, organizationId);

    }

    @Override
    public Optional<UserSnapshot> findUser(UUID userId, UUID organizationId) {
        return userRepository.findUser(userId, organizationId).map(m -> new UserSnapshot(m.id().value(), m.firstName() + " " + m.lastName(), m.email()));
    }

    @Override
    public List<UserSuggestion> findUserSuggestions(UUID organizationId, String search, Set<String> roles) {
        if (roles == null) roles = new HashSet<>();

        Set<UserRole> mappedRoles = roles.stream().map(UserRole::from).collect(Collectors.toSet());

        return userSuggestionsQuery.find(organizationId, search, mappedRoles);
    }
}
