package com.pl.hragency.organization.application.handler;

import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import com.pl.hragency.organization.domain.model.Organization;
import com.pl.hragency.organization.application.port.OrganizationRepository;
import com.pl.hragency.shared.event.EventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service

public class CreateOrganizationHandler {
    private final OrganizationRepository repository;
    private final EventPublisher publisher;

    public CreateOrganizationHandler(
            OrganizationRepository repository, EventPublisher publisher) {

        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
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

        var event = new OrganizationCreatedEvent(
                organization.id().value(),
                organization.name(),
                organization.slug(),
                null,
                null,
                Instant.now());

        publisher.publish(event);

        return organization.id().value();
    }
}
