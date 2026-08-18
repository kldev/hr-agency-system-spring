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

    private String summary;
    private final Instant createdAt;


    private Candidate(
            CandidateId id,
            UUID organizationId,
            String email,
            String firstName,
            String lastName,
            String phone,
            String summary,
            CandidateStatus status,
            CandidateSource source,
            Instant createdAt) {

        this.id = Objects.requireNonNull(id);
        this.organizationId = Objects.requireNonNull(organizationId);

        this.email = normalizeEmail(requireText(email, "Email"));

        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.summary = summary;

        this.status = Objects.requireNonNull(status);
        this.source = Objects.requireNonNull(source);

        this.createdAt = Objects.requireNonNull(createdAt);
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
                "",
                CandidateStatus.ACTIVE,
                source,
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
            String summary,
            CandidateStatus status,
            CandidateSource source,
            Instant createdAt) {

        return new Candidate(
                id,
                organizationId,
                email,
                firstName,
                lastName,
                phone,
                summary,
                status,
                source,
                createdAt
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
    }

    public void updateSummary(String summary)
    {
        this.summary =  requireText(summary, "Summary");;
    }

    public void updateStatus(CandidateStatus status)
    {
        switch (status) {
            case ACTIVE -> activate();
            case BLOCKED ->  block();
            case ARCHIVED -> archive();
        }
    }

    public void updatePhone(String phone)
    {
        this.phone =  requireText(phone, "Phone");;
    }

    public void updateEmail(String email)
    {
        this.email = normalizeEmail( requireText(email, "Email"));
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
    }

    public void archive() {
        if (status == CandidateStatus.ARCHIVED) {
            return;
        }

        status = CandidateStatus.ARCHIVED;
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

    public String summary() {return  summary; }
}
