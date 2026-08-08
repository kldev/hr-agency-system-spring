package com.pl.hragency.company.domain.event;

import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.UserSnapshot;

import java.util.UUID;

public record CompanySalesOwnerChangedEvent(UUID companyId,
                                            UUID organizationId,
                                            UserSnapshot previousOwner,
                                            UserSnapshot currentOwner) implements DomainEvent {
}
