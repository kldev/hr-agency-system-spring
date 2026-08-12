package com.pl.hragency.company.application.service;

import com.pl.hragency.company.application.command.AssignSalesOwnerCommand;
import com.pl.hragency.company.application.port.CompanyRepository;
import com.pl.hragency.company.domain.event.CompanySalesOwnerChangedEvent;
import com.pl.hragency.company.domain.model.Company;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class AssignSalesPersonHandler {
    private final CompanyRepository repository;
    private final IdentityApi identityApi;
    private final EventPublisher eventPublisher;

    public AssignSalesPersonHandler(CompanyRepository repository,
                                    IdentityApi identityApi,
                                    EventPublisher eventPublisher) {
        this.repository = repository;
        this.identityApi = identityApi;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ExecutionContext context, CompanyId companyId, AssignSalesOwnerCommand command) {

       Company company = repository.findById(companyId, context.organizationId())
               .orElseThrow(() -> new IllegalStateException("Company not found by id"));


        // nowy owner
        var newSales =
                identityApi.findUser(
                                command.salesUserId(),
                                context.organizationId()
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Sales person does not belong to organization"
                                ));

        if (Objects.equals(
                company.salesOwnerId(),
                command.salesUserId()
        )) {
            return;
        }

        UserSnapshot previousOwner = null;

        if (company.salesOwnerId() != null) {

            var previousSales =
                    identityApi.findUser(
                            company.salesOwnerId(),
                            context.organizationId()
                    );

            previousOwner = previousSales
                    .map(user -> new UserSnapshot(
                            user.id(),
                            user.fullName(),
                            user.email()
                    ))
                    .orElse(null);
        }



        var currentOwner =
                new UserSnapshot(
                        newSales.id(),
                        newSales.fullName(),
                        newSales.email()
                );

        repository.assignSales(companyId, command.salesUserId());

        var event = new CompanySalesOwnerChangedEvent(companyId.value(),
                context.organizationId(),
                previousOwner, currentOwner,
                context.userId(), context.fullName(), Instant.now()
                );

        eventPublisher.publish(event);
    }
}
