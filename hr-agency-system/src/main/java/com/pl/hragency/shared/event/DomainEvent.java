package com.pl.hragency.shared.event;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID organizationId();
    UUID actorId();
    String actorName();
    Instant occurredOn();
}
