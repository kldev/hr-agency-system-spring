package com.pl.hragency.organization.application.result;

import java.util.UUID;

public record CreateOrganizationResult(UUID id, String name, String slug) {
}
