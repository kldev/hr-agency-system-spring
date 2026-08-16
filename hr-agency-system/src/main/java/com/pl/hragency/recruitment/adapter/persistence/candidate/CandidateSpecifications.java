package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class CandidateSpecifications {

    private CandidateSpecifications() {
    }

    public static Specification<CandidateJpaEntity> organizationId(
            UUID organizationId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<CandidateJpaEntity> status(
            CandidateStatus status) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<CandidateJpaEntity> email(
            String email) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("email"),
                        normalize(email)
                );
    }

    public static Specification<CandidateJpaEntity> emailContains(
            String email) {

        return (root, query, cb) ->
                cb.like(
                        root.get("email"),
                        "%" + normalize(email) + "%"
                );
    }

    public static Specification<CandidateJpaEntity> firstNameContains(
            String value) {

        return contains("firstName", value);
    }

    public static Specification<CandidateJpaEntity> lastNameContains(
            String value) {

        return contains("lastName", value);
    }

    public static Specification<CandidateJpaEntity> phoneContains(
            String value) {

        return contains("phone", value);
    }

    public static Specification<CandidateJpaEntity> search(
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized = "%" + value.trim().toLowerCase() + "%";

        return (root, query, cb) ->
                cb.or(
                        cb.like(
                                root.get("email"),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("firstName")),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("lastName")),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("phone")),
                                normalized
                        )
                );
    }

    private static Specification<CandidateJpaEntity> contains(
            String field,
            String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        String normalized =
                "%" + value.trim().toLowerCase() + "%";

        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get(field)),
                        normalized
                );
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase();
    }
}