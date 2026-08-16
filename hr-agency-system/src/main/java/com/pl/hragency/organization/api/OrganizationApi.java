package com.pl.hragency.organization.api;

import java.util.List;
import java.util.UUID;

public interface OrganizationApi {
    OrganizationSummary findBySlug(String slug);

    OrganizationSummary findById(UUID organizationId);

    UUID create(String name, String slug);

    boolean existsBySlug(String slug);

    List<OrganizationSummary> findAllActive();
}
