package com.pl.hragency.organization.api;

import java.util.UUID;

public record OrganizationSummary(
        UUID id,
        String name,
        String slug
) {
}
