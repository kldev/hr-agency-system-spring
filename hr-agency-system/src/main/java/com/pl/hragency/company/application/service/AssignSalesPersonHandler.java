package com.pl.hragency.company.application.service;

import com.pl.hragency.company.application.command.AssignSalesOwnerCommand;
import com.pl.hragency.company.application.port.CompanyRepository;
import com.pl.hragency.company.domain.event.CompanySalesOwnerChangedEvent;
import com.pl.hragency.company.domain.model.Company;
import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Transactional
@Service
public class AssignSalesPersonHandler {
    private final CompanyRepository companyRepository;
    private final IdentityApi identityApi;
    private final EventPublisher eventPublisher;

    public AssignSalesPersonHandler(CompanyRepository companyRepository, IdentityApi identityApi, EventPublisher eventPublisher) {
        this.companyRepository = companyRepository;
        this.identityApi = identityApi;
        this.eventPublisher = eventPublisher;
    }

    public void handle(CompanyId companyId, AssignSalesOwnerCommand command) {

       Company company = companyRepository.findById(companyId).orElseThrow(() -> new IllegalStateException("Company not found by id"));

        if (!identityApi.existsInOrganization(
                command.salesUserId(),
                company.organizationId().value()
        )) {
            throw new IllegalArgumentException(
                    "Sales person does not belong to company organization"
            );
        }

        var organizationId =
                company.organizationId().value();

        UserSnapshot previousOwner = null;

        if (company.salesOwnerId() != null) {

            var previousSales =
                    identityApi.findUser(
                            company.salesOwnerId(),
                            organizationId
                    );

            previousOwner = previousSales
                    .map(user -> new UserSnapshot(
                            user.id(),
                            user.fullName(),
                            user.email()
                    ))
                    .orElse(null);
        }

        // nowy owner
        var newSales =
                identityApi.findUser(
                                command.salesUserId(),
                                organizationId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Sales person does not belong to organization"
                                ));

        var currentOwner =
                new UserSnapshot(
                        newSales.id(),
                        newSales.fullName(),
                        newSales.email()
                );

        // nic się nie zmieniło
        if (Objects.equals(
                company.salesOwnerId(),
                command.salesUserId()
        )) {
            return;
        }

        companyRepository.assignSales(companyId, command.salesUserId());

        eventPublisher.publish(new CompanySalesOwnerChangedEvent(companyId.value(), organizationId, previousOwner, currentOwner));
    }
}
