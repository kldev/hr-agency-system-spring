package com.pl.hragency.recruitment.adapter.persistence.interview;

import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class InterviewReadSpecifications {
    private InterviewReadSpecifications() {
    }

    public static Specification<InterviewReadJpaEntity> organizationId(
            UUID organizationId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<InterviewReadJpaEntity> createdBy(
            UUID value) {

        if (value == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("createdBy"),
                        value
                );
    }

    public static Specification<InterviewReadJpaEntity> from(
            Instant value) {

        if (value == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("scheduledAt"),
                        value
                );
    }

    public static Specification<InterviewReadJpaEntity> to(
            Instant value) {

        if (value == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.lessThanOrEqualTo(
                        root.get("scheduledAt"),
                        value
                );
    }


    public static Specification<InterviewReadJpaEntity> search(
            String value) {

        if (value == null || value.isBlank()) {
            return Specification.anyOf();
        }

        String normalized =
                "%" + value.trim().toLowerCase() + "%";

        return (root, query, cb) ->
                cb.or(
                        cb.like(
                                cb.lower(root.get("candidateEmail")),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("candidateName")),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("feedback")),
                                normalized
                        )
                );
    }

}
