package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.domain.model.SalesActivityType;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public final class SalesOpportunityActivitySpecifications {

    private SalesOpportunityActivitySpecifications() {
    }

    public static Specification<SalesOpportunityActivityJpaEntity> organizationId(
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

    public static Specification<SalesOpportunityActivityJpaEntity> salesOpportunityId(
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

    public static Specification<SalesOpportunityActivityJpaEntity> contactId(
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

    public static Specification<SalesOpportunityActivityJpaEntity> type(
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

    public static Specification<SalesOpportunityActivityJpaEntity> occurredFrom(
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

    public static Specification<SalesOpportunityActivityJpaEntity> occurredTo(
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

    public static Specification<SalesOpportunityActivityJpaEntity> search(
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
                                cb.lower(root.get("subject")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("description")),
                                value
                        )
                );
    }

    public static Specification<SalesOpportunityActivityJpaEntity> occurredDesc() {
        return (root, query, cb) -> {
            query.orderBy(
                    cb.desc(root.get("occurredAt"))
            );

            return cb.conjunction();
        };
    }
}