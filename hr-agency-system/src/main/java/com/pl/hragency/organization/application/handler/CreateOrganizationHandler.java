package com.pl.hragency.organization.application.handler;

import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.result.CreateOrganizationResult;
import com.pl.hragency.organization.domain.event.OrganizationCreateAdminEvent;
import com.pl.hragency.organization.domain.event.OrganizationCreatedEvent;
import com.pl.hragency.organization.domain.model.Organization;
import com.pl.hragency.organization.application.port.OrganizationRepository;
import com.pl.hragency.shared.event.EventPublisher;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateOrganizationHandler {
    private final OrganizationRepository repository;
    private final EventPublisher publisher;

    public CreateOrganizationHandler(
            OrganizationRepository repository,
            EventPublisher publisher) {

        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public CreateOrganizationResult handle(CreateOrganizationCommand command) {

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
                organization.slug().value(),
                null,
                null,
                Instant.now());
        publisher.publish(event);

        if (command.organizationAdmin() != null) {
            var createAdminEvent = new OrganizationCreateAdminEvent(organization.id().value(),
                    command.organizationAdmin().email(),
                    command.organizationAdmin().firstName(),
                    command.organizationAdmin().latName(),
                    command.organizationAdmin().password(),
                    null, "SYSTEM", Instant.now());
            publisher.publish(createAdminEvent);

        }

        return new CreateOrganizationResult(organization.id().value(),
                organization.name(),
                organization.slug().value());
    }
}
