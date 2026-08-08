package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class SalesOpportunitySpecifications {

    private SalesOpportunitySpecifications() {
    }

    public static Specification<SalesOpportunityJpaEntity> organizationId(
            UUID organizationId
    ) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<SalesOpportunityJpaEntity> companyId(
            UUID companyId
    ) {
        if (companyId == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("companyId"),
                        companyId
                );
    }

    public static Specification<SalesOpportunityJpaEntity> stage(
            SalesOpportunityStage stage
    ) {
        if (stage == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("stage"),
                        stage
                );
    }

    public static Specification<SalesOpportunityJpaEntity> salesOwnerId(
            UUID salesOwnerId
    ) {
        if (salesOwnerId == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("salesOwnerId"),
                        salesOwnerId
                );
    }

    public static Specification<SalesOpportunityJpaEntity> search(
            String search
    ) {
        if (search == null || search.isBlank()) {
            return Specification.allOf();
        }

        var value = "%" +
                search.trim().toLowerCase() +
                "%";

        return (root, query, cb) ->
                cb.or(
                        cb.like(
                                cb.lower(root.get("title")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("description")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("lostReason")),
                                value
                        )
                );
    }
}