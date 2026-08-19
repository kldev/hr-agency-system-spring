package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class JobApplicationReadSpecifications {

    private JobApplicationReadSpecifications() {
    }

    public static Specification<JobApplicationReadJpaEntity> organizationId(
            UUID organizationId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<JobApplicationReadJpaEntity> companyId(
            UUID companyId) {

        if (companyId == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("companyId"),
                        companyId
                );
    }

    public static Specification<JobApplicationReadJpaEntity> status(
            JobApplicationStatus status) {

        if (status == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<JobApplicationReadJpaEntity> postingId(
            UUID postingId) {

        if (postingId == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("jobPostingId"),
                        postingId
                );
    }

    public static Specification<JobApplicationReadJpaEntity> recruiterId(
            UUID recruiterId) {

        if (recruiterId == null) {
            return Specification.anyOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("recruiterId"),
                        recruiterId
                );
    }

    public static Specification<JobApplicationReadJpaEntity> search(
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
                                cb.lower(root.get("candidateFirstName")),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("candidateLastName")),
                                normalized
                        ),
                        cb.like(
                                cb.lower(root.get("recruiterFullName")),
                                normalized
                        )
                );
    }
}