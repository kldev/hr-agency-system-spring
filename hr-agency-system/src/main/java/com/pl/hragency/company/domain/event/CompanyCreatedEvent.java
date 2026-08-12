package com.pl.hragency.company.domain.event;

import com.pl.hragency.shared.event.DomainEvent;

import java.time.Instant;
import java.util.UUID;

public record CompanyCreatedEvent( UUID companyId,
                                   UUID organizationId,
                                   String name,
                                   String countryCode,
                                   String taxNumber,
                                   UUID actorId,
                                   String actorName,
                                   Instant occurredOn) implements DomainEvent {
}
