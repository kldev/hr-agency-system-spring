package com.pl.hragency.organization.application.handler;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.organization.application.command.CreateOrganizationCommand;
import com.pl.hragency.organization.application.result.CreateOrganizationResult;
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
    private final IdentityApi identityApi;

    public CreateOrganizationHandler(
            OrganizationRepository repository, EventPublisher publisher, IdentityApi identityApi) {

        this.repository = repository;
        this.publisher = publisher;
        this.identityApi = identityApi;
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

        if (command.organizationAdmin() != null) {
            var admin = command.organizationAdmin();
            identityApi.createUser(
                    admin.email(),
                    admin.firstName(),
                    admin.latName(),
                    "ADMIN",
                    organization.id().value(),
                    admin.password());
        }


        publisher.publish(event);

        return new CreateOrganizationResult(organization.id().value(),
                organization.name(),
                organization.slug().value());
    }
}
