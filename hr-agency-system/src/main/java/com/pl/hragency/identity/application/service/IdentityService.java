package com.pl.hragency.identity.application.service;

import com.pl.hragency.identity.api.CurrentIntegrationClient;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.api.UserSuggestion;
import com.pl.hragency.identity.application.command.CreateUserCommand;
import com.pl.hragency.identity.application.handler.CreateUserHandler;
import com.pl.hragency.identity.application.port.*;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.application.query.UserSuggestionsQuery;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.identity.domain.model.PlatformOwner;
import com.pl.hragency.identity.domain.model.PlatformRole;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IdentityService implements IdentityApi {
    private final UserRepository userRepository;
    private final CurrentPrincipalProvider currentUserProvider;
    private final AuthorizationService authorizationService;
    private final UserSuggestionsQuery userSuggestionsQuery;
    private final CreateUserHandler createUserHandler;
    private final PlatformOwnerRepository platformUserRepository;
    private final PasswordHasher hasher;

    public IdentityService(UserRepository userRepository,
                           CurrentPrincipalProvider currentUserProvider,
                           AuthorizationService authorizationService,
                           UserSuggestionsQuery userSuggestionsQuery,
                           CreateUserHandler createUserHandler, PlatformOwnerRepository platformUserRepository, PasswordHasher hasher) {
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
        this.authorizationService = authorizationService;
        this.userSuggestionsQuery = userSuggestionsQuery;
        this.createUserHandler = createUserHandler;
        this.platformUserRepository = platformUserRepository;
        this.hasher = hasher;
    }

    @Override
    public CurrentUser getCurrentUser() {
        return currentUserProvider.getRequiredUser();
    }

    @Override
    public CurrentIntegrationClient gCurrentIntegrationClient() {
        return currentUserProvider.getRequiredIntegration();
    }

    @Override
    public UUID createUser(String email, String firstName, String lastName, String role, UUID organizationId, String password) {

        var command = new CreateUserCommand(email, password, firstName, lastName, OrganizationRole.from(role));
        var context = new ExecutionContext(organizationId, UUID.randomUUID(), "System");

        return createUserHandler.handle(context, command).value();
    }

    @Override
    public UUID createPlatformUser(String email, String role, String password) {
        var user = PlatformOwner.create(email, PlatformRole.valueOf(role), hasher.hash(password));

        platformUserRepository.save(user);

        return user.id().value();
    }

    @Override
    public void requireRole(String role) {
        authorizationService.requireRole(OrganizationRole.from(role));
    }

    @Override
    public boolean isCurrentUserSales() {
        var user = getCurrentUser();
        if (user == null) return false;

        return user.role() == OrganizationRole.SALES;
    }

    @Override
    public boolean isCurrentUserRecruiter() {
        var user = getCurrentUser();
        if (user == null) return false;

        return user.role() == OrganizationRole.RECRUITER;
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

        Set<OrganizationRole> mappedRoles = roles.stream().map(OrganizationRole::from).collect(Collectors.toSet());

        return userSuggestionsQuery.find(organizationId, search, mappedRoles);
    }
}
