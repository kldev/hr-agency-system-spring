package com.pl.hragency.organization.application.port;

import com.pl.hragency.organization.domain.model.Organization;

import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {
    Organization save(Organization organization);

    Optional<Organization> findById(UUID id);

    Optional<Organization> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsById(UUID id);
}
