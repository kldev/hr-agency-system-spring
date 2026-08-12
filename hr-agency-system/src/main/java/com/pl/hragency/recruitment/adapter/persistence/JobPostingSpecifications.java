package com.pl.hragency.recruitment.adapter.persistence;


import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class JobPostingSpecifications {
    private JobPostingSpecifications() {
    }

    public static Specification<JobPostingJpaEntity> organizationId(
            UUID organizationId
    ) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<JobPostingJpaEntity> recruiterId(
            UUID recruiterId
    ) {
        if (recruiterId == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("recruiterId"),
                        recruiterId
                );
    }

    public static Specification<JobPostingJpaEntity> search(
            String search
    ) {
        if (search == null || search.isBlank()) {
            return Specification.allOf();
        }

        String value = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) ->
                cb.or(
                        cb.like(
                                cb.lower(root.get("title")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("summary")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("description")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("location")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("countryCode")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("salaryCurrency")),
                                value
                        )
                );
    }

    public static Specification<JobPostingJpaEntity> employmentType(
            EmploymentType employmentType
    ) {
        if (employmentType == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("employmentType"),
                        employmentType
                );
    }

    public static Specification<JobPostingJpaEntity> workMode(
            WorkMode workMode
    ) {
        if (workMode == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("workMode"),
                        workMode
                );
    }
}
