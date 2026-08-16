package com.pl.hragency.organization.domain.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Organization {

    private final OrganizationId id;
    private String name;
    private OrganizationSlug slug;
    private final Instant createdAt;

    private Organization(
            OrganizationId id,
            String name,
            OrganizationSlug slug,
            Instant createdAt) {

        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.slug = Objects.requireNonNull(slug);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Organization create(
            String name,
            String slug) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization name cannot be blank");
        }

        if (slug == null || slug.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization slug cannot be blank");
        }

        return new Organization(
                OrganizationId.newId(),
                name.trim(),
                new OrganizationSlug(slug.trim().toLowerCase()),
                Instant.now()
        );
    }

    public static Organization rehydrate(
            UUID id,
            String name,
            String slug,
            Instant createdAt) {

        return new Organization(
                new OrganizationId(id),
                name,
                new OrganizationSlug(slug),
                createdAt
        );
    }

    public OrganizationId id() {
        return id;
    }

    public String name() {
        return name;
    }

    public OrganizationSlug slug() {
        return slug;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void rename(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Organization name cannot be blank");
        }

        this.name = name.trim();
    }
}
