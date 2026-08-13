package com.pl.hragency.identity.domain.model;

import java.time.Instant;

public class PlatformOwner {
    private final PlatformOwnerId id;

    private final String email;
    private final PlatformRole role;
    private final String passwordHash;

    private final Instant createdAt;

    private PlatformOwner(
            PlatformOwnerId id,
            String email,
            PlatformRole role,
            String passwordHash,
            Instant createdAt) {

        this.id = id;
        this.email = normalizeEmail(email);

        this.role = role;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    public static PlatformOwner rehydrate(
            PlatformOwnerId id,
            String email,
            PlatformRole role,
            String passwordHash,
            Instant createdAt) {

        return new PlatformOwner(
                id,
                email,
                role,
                passwordHash,
                createdAt
        );
    }

    public static PlatformOwner create(
            String email,
            PlatformRole role,
            String passwordHash) {

        return new PlatformOwner(
                PlatformOwnerId.newId(),
                email,
                role,
                passwordHash,
                Instant.now()
        );
    }

    public PlatformOwnerId id() {
        return id;
    }

    public String email() {
        return email;
    }

    public PlatformRole role() {
        return role;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public String passwordHash() { return passwordHash; }
}
