package com.pl.hragency.recruitment.domain.model.candidate;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Candidate {

    private final CandidateId id;
    private final UUID organizationId;

    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    private CandidateStatus status;
    private final CandidateSource source;

    private final Instant createdAt;
    private Instant updatedAt;

    private Candidate(
            CandidateId id,
            UUID organizationId,
            String email,
            String firstName,
            String lastName,
            String phone,
            CandidateStatus status,
            CandidateSource source,
            Instant createdAt,
            Instant updatedAt) {

        this.id = Objects.requireNonNull(id);
        this.organizationId = Objects.requireNonNull(organizationId);

        this.email = normalizeEmail(requireText(email, "Email"));

        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;

        this.status = Objects.requireNonNull(status);
        this.source = Objects.requireNonNull(source);

        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);
    }

    public static Candidate create(
            UUID organizationId,
            String email,
            String firstName,
            String lastName,
            String phone,
            CandidateSource source) {

        var now = Instant.now();

        return new Candidate(
                CandidateId.newId(),
                organizationId,
                email,
                firstName,
                lastName,
                phone,
                CandidateStatus.ACTIVE,
                source,
                now,
                now
        );
    }

    public static Candidate rehydrate(
            CandidateId id,
            UUID organizationId,
            String email,
            String firstName,
            String lastName,
            String phone,
            CandidateStatus status,
            CandidateSource source,
            Instant createdAt,
            Instant updatedAt) {

        return new Candidate(
                id,
                organizationId,
                email,
                firstName,
                lastName,
                phone,
                status,
                source,
                createdAt,
                updatedAt
        );
    }

    public void update(
            String email,
            String firstName,
            String lastName,
            String phone) {

        this.email = requireText(email, "Email");
        this.firstName = requireText(firstName, "First name");
        this.lastName = requireText(lastName, "Last name");
        this.phone = phone;

        touch();
    }

    public void updatePhone(String phone)
    {
        this.phone =  requireText(phone, "Phone");;
        touch();
    }

    public void updateEmail(String email)
    {
        this.phone = normalizeEmail( requireText(email, "Email"));
        touch();
    }

    public void block() {
        if (status == CandidateStatus.ARCHIVED) {
            throw new IllegalStateException(
                    "Archived candidate cannot be blocked"
            );
        }

        if (status == CandidateStatus.BLOCKED) {
            return;
        }

        status = CandidateStatus.BLOCKED;
        touch();
    }

    public void activate() {
        if (status == CandidateStatus.ARCHIVED) {
            throw new IllegalStateException(
                    "Archived candidate cannot be activated"
            );
        }

        if (status == CandidateStatus.ACTIVE) {
            return;
        }

        status = CandidateStatus.ACTIVE;
        touch();
    }

    public void archive() {
        if (status == CandidateStatus.ARCHIVED) {
            return;
        }

        status = CandidateStatus.ARCHIVED;
        touch();
    }

    private void touch() {
        updatedAt = Instant.now();
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be empty");
        }

        return value.trim();
    }

    public CandidateId id() {
        return id;
    }

    public UUID organizationId() {
        return organizationId;
    }

    public String email() {
        return email;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String phone() {
        return phone;
    }

    public CandidateStatus status() {
        return status;
    }

    public CandidateSource source() { return source; }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }
}
