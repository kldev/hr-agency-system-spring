package com.pl.hragency.identity.application.query;

import com.pl.hragency.identity.domain.model.OrganizationRole;

import java.util.UUID;

public record UserListItem(
        UUID id,
        String email,
        String firstName,
        String lastName,
        OrganizationRole role
) {
}
