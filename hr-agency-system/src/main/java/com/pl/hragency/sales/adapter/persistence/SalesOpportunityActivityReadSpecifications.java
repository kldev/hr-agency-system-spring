package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesActivityType;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public class SalesOpportunityActivityReadSpecifications {
    private SalesOpportunityActivityReadSpecifications() {
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> organizationId(
            UUID organizationId
    ) {
        if (organizationId == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> salesOpportunityId(
            UUID salesOpportunityId
    ) {
        if (salesOpportunityId == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("salesOpportunityId"),
                        salesOpportunityId
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> contactId(
            UUID contactId
    ) {
        if (contactId == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("contactId"),
                        contactId
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> type(
            SalesActivityType type
    ) {
        if (type == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("type"),
                        type
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> occurredFrom(
            Instant occurredFrom
    ) {
        if (occurredFrom == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(
                        root.get("occurredAt"),
                        occurredFrom
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> occurredTo(
            Instant occurredTo
    ) {
        if (occurredTo == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.lessThan(
                        root.get("occurredAt"),
                        occurredTo
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> search(
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
                                cb.lower(root.get("note")),
                                value
                        )
                );
    }

    public static Specification<SalesOpportunityActivityReadJpaEntity> occurredDesc() {
        return (root, query, cb) -> {
            query.orderBy(
                    cb.desc(root.get("occurredAt"))
            );

            return cb.conjunction();
        };
    }
}
