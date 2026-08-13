package com.pl.hragency.identity.domain.model;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

public final class IntegrationClient {

    private final IntegrationClientId id;
    private final UUID organizationId;

    private String name;
    private final String keyId;
    private final String secretHash;

    private final Set<IntegrationScope> scopes;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant revokedAt;

    private IntegrationClient(
            IntegrationClientId id,
            UUID organizationId,
            String name,
            String keyId,
            String secretHash,
            Set<IntegrationScope> scopes,
            Instant revokedAt,
            Instant createdAt,
            Instant updatedAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.name = requireName(name);
        this.keyId = requireValue(keyId, "Key ID");
        this.secretHash = requireValue(secretHash, "Secret hash");

        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException("At least one integration scope is required");
        }

        this.scopes = EnumSet.copyOf(scopes);
        this.revokedAt = revokedAt;

        this.createdAt = createdAt;
        this.updatedAt = updatedAt;

    }

    public static IntegrationClient create(
            UUID organizationId,
            String name,
            String keyId,
            String secretHash,
            Set<IntegrationScope> scopes,
            Instant now) {

        if (organizationId == null) {
            throw new IllegalArgumentException("Organization ID is required");
        }

        if (now == null) {
            throw new IllegalArgumentException("Created date is required");
        }

        return new IntegrationClient(
                IntegrationClientId.newId(),
                organizationId,
                name,
                keyId,
                secretHash,
                scopes,
                null,
                now,
                now
        );
    }

    public static IntegrationClient rehydrate(
            IntegrationClientId id,
            UUID organizationId,
            String name,
            String keyId,
            String secretHash,
            Set<IntegrationScope> scopes,
            Instant revokedAt,
            Instant createdAt,
            Instant updatedAt) {

        return new IntegrationClient(
                id,
                organizationId,
                name,
                keyId,
                secretHash,
                scopes,
                revokedAt,
                createdAt,
                updatedAt
        );
    }

    public void rename(String name, Instant now) {
        this.name = requireName(name);
        touch(now);
    }

    public void changeScopes(Set<IntegrationScope> scopes, Instant now) {

        if (scopes == null || scopes.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one integration scope is required"
            );
        }

        this.scopes.clear();
        this.scopes.addAll(scopes);

        touch(now);
    }

    public void activate(Instant now) {

        if (revokedAt == null) {
            return;
        }

        revokedAt = null;
        touch(now);
    }

    public void deactivate(Instant now) {

        if (revokedAt != null) {
            return;
        }

        revokedAt = now;
        touch(now);
    }

    public boolean hasScope(IntegrationScope scope) {
        return revokedAt == null && scopes.contains(scope);
    }

    private void touch(Instant now) {
        if (now == null) {
            throw new IllegalArgumentException("Updated date is required");
        }

        updatedAt = now;
    }

    private static String requireName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Integration client name is required");
        }

        return value.trim();
    }

    private static String requireValue(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }

        return value;
    }

    public IntegrationClientId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public String name() {
        return name;
    }

    public String keyId() {
        return keyId;
    }

    public String secretHash() {
        return secretHash;
    }

    public Set<IntegrationScope> scopes() {
        return Set.copyOf(scopes);
    }

    public Instant revokedAt() {
        return revokedAt;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

}

