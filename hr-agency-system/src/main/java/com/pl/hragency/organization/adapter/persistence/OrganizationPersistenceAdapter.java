package com.pl.hragency.organization.adapter.persistence;

import com.pl.hragency.organization.domain.model.Organization;
import com.pl.hragency.organization.application.port.OrganizationRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class OrganizationPersistenceAdapter implements OrganizationRepository {
    private final SpringDataOrganizationRepository repository;
    private final OrganizationMapper mapper;

    public OrganizationPersistenceAdapter(
            SpringDataOrganizationRepository repository,
            OrganizationMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Organization save(Organization organization) {

        var entity = mapper.toEntity(organization);

        return mapper.toDomain(
                repository.save(entity)
        );
    }

    @Override
    public Optional<Organization> findById(UUID id) {

        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Organization> findBySlug(String slug) {

        return repository.findBySlug(slug)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsBySlug(String slug) {

        return repository.existsBySlug(slug);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }
}
