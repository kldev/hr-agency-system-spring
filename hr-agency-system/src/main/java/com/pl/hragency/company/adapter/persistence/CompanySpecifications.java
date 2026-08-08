package com.pl.hragency.company.adapter.persistence;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class CompanySpecifications {

    private CompanySpecifications() {
    }

    public static Specification<CompanyJpaEntity> organizationId(
            UUID organizationId) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<CompanyJpaEntity> search(
            String search) {

        if (search == null || search.isBlank()) {
            return Specification.allOf();
        }

        String value = "%" + search.trim().toLowerCase() + "%";

        return (root, query, cb) ->
                cb.or(
                        cb.like(
                                cb.lower(root.get("name")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("taxId")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("registrationNumber")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("city")),
                                value
                        )
                );
    }

    public static Specification<CompanyJpaEntity> countryCode(
            String countryCode) {

        if (countryCode == null || countryCode.isBlank()) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("countryCode"),
                        countryCode
                );
    }

}
