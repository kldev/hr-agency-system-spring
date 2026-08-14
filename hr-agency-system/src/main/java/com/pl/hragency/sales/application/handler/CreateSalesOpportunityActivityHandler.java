package com.pl.hragency.sales.application.handler;


import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityActivityCommand;
import com.pl.hragency.sales.application.port.SalesOpportunityActivityRepository;
import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
import com.pl.hragency.sales.domain.event.SalesOpportunityActivityCreatedEvent;
import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityActivity;
import com.pl.hragency.sales.domain.model.SalesOpportunityActivityId;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CreateSalesOpportunityActivityHandler {
    private final SalesOpportunityActivityRepository repository;
    private final SalesOpportunityRepository opportunityRepository;
    private final EventPublisher eventPublisher;
    private final IdentityApi identityApi;

    public CreateSalesOpportunityActivityHandler(SalesOpportunityActivityRepository repository,
                                                 SalesOpportunityRepository opportunityRepository,
                                                 EventPublisher eventPublisher,
                                                 IdentityApi identityApi) {
        this.repository = repository;
        this.opportunityRepository = opportunityRepository;
        this.eventPublisher = eventPublisher;
        this.identityApi = identityApi;
    }

    @Transactional
    public SalesOpportunityActivityId handle(ExecutionContext context,SalesOpportunityId salesOpportunityId, CreateSalesOpportunityActivityCommand command) {

        var opportunity = opportunityRepository.findById(context.organizationId(), salesOpportunityId)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.SalesOpportunity, salesOpportunityId.value()));

        var activity = SalesOpportunityActivity.create(
                context.organizationId(),
                salesOpportunityId,
                command.type(),
                command.note(), Instant.now(), context.userId());

        repository.save(activity);

        var userSnapshot = identityApi.findUser(context.userId(), context.organizationId()).orElse(null);

        var event = new SalesOpportunityActivityCreatedEvent(activity.id().value(),
                opportunity.id().value(),
                context.organizationId(),
                userSnapshot,
                context.userId(),
                context.fullName(),
                activity.occurredAt());

        eventPublisher.publish(event);

        return activity.id();
    }
}
