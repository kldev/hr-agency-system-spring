package com.pl.hragency.identity.application.security;

import com.pl.hragency.identity.domain.model.IntegrationScope;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public record IntegrationAuthentication(
        UUID clientId,
        UUID organizationId,
        String clientName,
        Set<IntegrationScope> scopes
) implements Authentication {

    public IntegrationAuthentication {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        if (organizationId == null) {
            throw new IllegalArgumentException("Organization ID is required");
        }
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("Client name is required");
        }
        if (scopes == null) {
            throw new IllegalArgumentException("Scopes are required");
        }
        scopes = Set.copyOf(scopes);
    }

    public boolean hasScope(IntegrationScope scope) {
        return scopes.contains(scope);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return scopes.stream() .map(scope ->
                new SimpleGrantedAuthority( "SCOPE_" + scope.name() )) .toList();
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return null;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return this;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        if (!authenticated) { throw new IllegalArgumentException( "IntegrationAuthentication is immutable" ); }
    }

    @Override
    public String getName() {
        return clientId.toString();
    }
}