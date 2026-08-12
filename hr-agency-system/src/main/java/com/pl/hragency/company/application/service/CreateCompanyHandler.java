package com.pl.hragency.company.application.service;

import com.pl.hragency.company.application.command.CreateCompanyCommand;
import com.pl.hragency.company.application.port.CompanyRepository;
import com.pl.hragency.company.domain.event.CompanyCreatedEvent;
import com.pl.hragency.company.domain.event.CompanySalesOwnerChangedEvent;
import com.pl.hragency.company.domain.model.*;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class CreateCompanyHandler {

    private final CompanyRepository companyRepository;
    private final EventPublisher  eventPublisher;
    private final IdentityApi identityApi;

    public CreateCompanyHandler(CompanyRepository companyRepository,
                                EventPublisher eventPublisher, IdentityApi identityApi) {
        this.companyRepository = companyRepository;
        this.eventPublisher = eventPublisher;
        this.identityApi = identityApi;
    }

    @Transactional
    public CompanyId handle(ExecutionContext context, CreateCompanyCommand command, boolean assignSales) {

        UUID organizationId = context.organizationId();

        if (companyRepository.exists(command.taxNumber(), organizationId)) {
            throw new IllegalArgumentException(
                    "Company with tax Id %s already exists".formatted(command.taxNumber()));
        }
        Address address =  new Address(new CountryCode(command.countryCode()),
                command.city(),
                command.street(),
                command.postalCode());

        Company company = Company.create(new CompanyOrganizationId(organizationId),
                command.name(),
                new TaxId(command.taxNumber()),
               address,
               new RegistrationNumber(command.registrationNumber()),
               null);

        companyRepository.save(company);

        var event = new CompanyCreatedEvent(company.id().value(),
                company.organizationId().value(),
                company.name(),
                company.address().countryCode().value(),
                company.taxId().value(),
                context.userId(), context.fullName(), Instant.now());

        eventPublisher.publish(event);

        if (assignSales) {
            companyRepository.assignSales(company.id(), context.userId());

            // nowy owner
            var newSales =
                    identityApi.findUser(
                                    context.userId(),
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

            var assignOwnerEvent = new CompanySalesOwnerChangedEvent(company.id().value(), organizationId, null,
                    currentOwner, context.userId(), context.fullName(), Instant.now());

            eventPublisher.publish(assignOwnerEvent);
        }

        return company.id();
    }
}
