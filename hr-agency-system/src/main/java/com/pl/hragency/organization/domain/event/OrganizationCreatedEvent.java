package com.pl.hragency.organization.domain.event;



import com.pl.hragency.shared.event.DomainEvent;
import org.springframework.modulith.NamedInterface;

import java.util.UUID;

@NamedInterface
public record OrganizationCreatedEvent(UUID organizationId, String name, String slug) implements DomainEvent {
}
