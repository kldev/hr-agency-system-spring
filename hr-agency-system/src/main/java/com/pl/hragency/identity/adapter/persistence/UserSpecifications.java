package com.pl.hragency.identity.adapter.persistence;

import com.pl.hragency.identity.domain.model.OrganizationRole;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;
import java.util.UUID;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<UserJpaEntity> organizationId(
            UUID organizationId
    ) {
        return (root, query, cb) ->
                cb.equal(
                        root.get("organizationId"),
                        organizationId
                );
    }

    public static Specification<UserJpaEntity> search(
            String search
    ) {
        if (search == null || search.isBlank()) {
            return Specification.allOf();
        }

        String value = "%" +
                search.trim().toLowerCase() +
                "%";

        return (root, query, cb) ->
                cb.or(
                        cb.like(
                                cb.lower(root.get("firstName")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("lastName")),
                                value
                        ),
                        cb.like(
                                cb.lower(root.get("email")),
                                value
                        )
                );
    }

    public static Specification<UserJpaEntity> roles(
            Set<OrganizationRole> roles
    ) {
        if (roles == null || roles.isEmpty()) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                root.get("role").in(roles);
    }

    public static Specification<UserJpaEntity> hasRole(
            OrganizationRole role
    ) {
        if (role == null) {
            return Specification.allOf();
        }

        return (root, query, cb) ->
                cb.equal(
                        root.get("role"),
                        role
                );
    }
}