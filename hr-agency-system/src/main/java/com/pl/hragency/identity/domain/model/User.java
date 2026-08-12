package com.pl.hragency.identity.domain.model;

import java.time.Instant;

public class User {

    private final UserId id;
    private final UserOrganizationId organizationId;

    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;
    private String passwordHash;

    private final Instant createdAt;

    private User(
            UserId id,
            UserOrganizationId organizationId,
            String email,
            String firstName,
            String lastName,
            UserRole role,
            String passwordHash,
            Instant createdAt) {

        this.id = id;
        this.organizationId = organizationId;
        this.email = normalizeEmail(email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    public static User rehydrate(
            UserId id,
            UserOrganizationId organizationId,
            String email,
            String firstName,
            String lastName,
            UserRole role,
            String passwordHash,
            Instant createdAt) {

        return new User(
                id,
                organizationId,
                email,
                firstName,
                lastName,
                role,
                passwordHash,
                createdAt
        );
    }

    public static User create(
            UserOrganizationId organizationId,
            String email,
            String firstName,
            String lastName,
            UserRole role,
            String passwordHash) {

        return new User(
                UserId.newId(),
                organizationId,
                email,
                firstName,
                lastName,
                role,
                passwordHash,
                Instant.now()
        );
    }

    public UserId id() {
        return id;
    }

    public UserOrganizationId organizationId() {
        return organizationId;
    }

    public String email() {
        return email;
    }

    public UserRole role() {
        return role;
    }

    public String firstName() { return firstName; }
    public String lastName() { return lastName; }

    public Instant createdAt() {
        return createdAt;
    }

    public String passwordHash() { return passwordHash; }
}
