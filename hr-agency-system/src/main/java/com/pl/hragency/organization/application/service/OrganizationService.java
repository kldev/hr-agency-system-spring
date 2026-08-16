package com.pl.hragency.organization.application.service;

import com.pl.hragency.organization.api.OrganizationApi;
import com.pl.hragency.organization.api.OrganizationSummary;
import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.handler.CreateOrganizationHandler;
import com.pl.hragency.organization.application.port.OrganizationRepository;
import com.pl.hragency.organization.domain.model.Organization;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganizationService implements OrganizationApi {
    private final CreateOrganizationHandler handler;
    private final OrganizationRepository repository;

    public OrganizationService(CreateOrganizationHandler handler, OrganizationRepository repository) {
        this.handler = handler;
        this.repository = repository;
    }

    @Override
    public OrganizationSummary findBySlug(String slug) {
        return repository.findBySlug(slug).map(this::toSummary).orElseThrow(() ->
                new IllegalArgumentException(
                        "Organization not found: " + slug
                ));
    }

    @Override
    public OrganizationSummary findById(UUID organizationId) {
        return repository.findById(organizationId).map(this::toSummary).orElseThrow(() ->
                new IllegalArgumentException(
                        "Organization not found: " + organizationId
                ));
    }

    @Override
    public UUID create(String name, String slug) {
               return handler.handle(
                new CreateOrganizationCommand(name, slug, null)).id();
    }

    @Override
    public boolean existsBySlug(String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public List<OrganizationSummary> findAllActive() {
        return repository.findAllActive()
                .stream().map(this::toSummary).toList();
    }

    private OrganizationSummary toSummary(
            Organization organization) {

        return new OrganizationSummary(
                organization.id().value(),
                organization.name(),
                organization.slug().value()
        );
    }
}
