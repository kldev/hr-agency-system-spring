package com.pl.hragency.sales.application.service;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.sales.application.command.CreateSalesOpportunityCommand;
import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
import com.pl.hragency.sales.domain.event.SalesOpportunityCreatedEvent;
import com.pl.hragency.sales.domain.exception.CompanyNotFoundException;
import com.pl.hragency.sales.domain.model.SalesOpportunity;
import com.pl.hragency.sales.domain.model.SalesOpportunityId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateSalesOpportunityHandler {

    private final SalesOpportunityRepository repository;
    private final IdentityApi identityApi;
    private final EventPublisher eventPublisher;
    private final CompanyApi companyApi;

    public CreateSalesOpportunityHandler(
            SalesOpportunityRepository repository,
            IdentityApi identityApi, EventPublisher eventPublisher, CompanyApi companyApi
    ) {
        this.repository = repository;
        this.identityApi = identityApi;
        this.eventPublisher = eventPublisher;
        this.companyApi = companyApi;
    }

    @Transactional
    public SalesOpportunityId handle(
            ExecutionContext context,
            CreateSalesOpportunityCommand command
    ) {
        UUID salesOwnerId = command.salesOwnerId() != null ? command.salesOwnerId()
                : identityApi.isCurrentUserSales() ? context.userId() : null;

        if (salesOwnerId == null) {
            throw new IllegalArgumentException("No salesOwnerId provided");
        }

        var exits = companyApi.exists(context.organizationId(), command.companyId());
        if (!exits) {
            throw new CompanyNotFoundException("Company with id " + command.companyId() + " not found");
        }

        var opportunity = SalesOpportunity.create(
                context.organizationId(),
                command.companyId(),
                command.title(),
                command.description(),
                command.expectedValue(),
                command.currencyCode(),
                command.expectedCloseDate(),
                salesOwnerId
        );

        repository.save(opportunity);

        UserSnapshot user = identityApi.findUser(salesOwnerId,context.organizationId() ).orElse(null);

        var event = new SalesOpportunityCreatedEvent(opportunity.id().value(),
                opportunity.companyId(), context.organizationId(), user,
                context.userId(), context.fullName(),
                Instant.now());

        eventPublisher.publish(event);

        return opportunity.id();
    }
}