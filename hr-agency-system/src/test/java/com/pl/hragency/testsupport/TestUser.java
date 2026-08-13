package com.pl.hragency.testsupport;

import com.pl.hragency.identity.domain.model.OrganizationRole;

import java.util.UUID;

public record TestUser(
        UUID id,
        UUID organizationId,
        String organizationSlug,
        String email,
        String password,
        OrganizationRole role
) {
}
