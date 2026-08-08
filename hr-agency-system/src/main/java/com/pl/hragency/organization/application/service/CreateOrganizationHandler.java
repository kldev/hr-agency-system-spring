package com.pl.hragency.organization.application.service;

import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import com.pl.hragency.organization.domain.model.Organization;
import com.pl.hragency.organization.application.port.OrganizationRepository;
import com.pl.hragency.shared.event.EventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class CreateOrganizationHandler {
    private final OrganizationRepository repository;
    private final EventPublisher publisher;

    public CreateOrganizationHandler(
            OrganizationRepository repository, EventPublisher publisher) {

        this.repository = repository;
        this.publisher = publisher;
    }

    public UUID handle(CreateOrganizationCommand command) {

        if (repository.existsBySlug(command.slug())) {
            throw new IllegalStateException(
                    "Organization already exists");
        }

        var organization = Organization.create(
                command.name(),
                command.slug()
        );

        repository.save(organization);

        publisher.publish(new OrganizationCreatedEvent(
                organization.id().value(),
                organization.name(),
                organization.slug()));

        return organization.id().value();
    }
}
