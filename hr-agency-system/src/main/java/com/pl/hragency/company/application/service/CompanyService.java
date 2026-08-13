package com.pl.hragency.company.application.service;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.company.api.CompanySuggestion;
import com.pl.hragency.company.application.command.CreateCompanyCommand;
import com.pl.hragency.company.application.command.CreateCompanyContactCommand;
import com.pl.hragency.company.application.handler.CreateCompanyContactHandler;
import com.pl.hragency.company.application.handler.CreateCompanyHandler;
import com.pl.hragency.company.application.query.CompanyListItem;
import com.pl.hragency.company.application.query.CompanyListQuery;
import com.pl.hragency.company.application.query.CompanyQueryService;
import com.pl.hragency.company.application.query.CompanySuggestionsQuery;
import com.pl.hragency.company.domain.model.*;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyService  implements CompanyApi {
    private final CreateCompanyHandler createCompanyHandler;
    private final CreateCompanyContactHandler createCompanyContactHandler;
    private final CompanyQueryService  companyQueryService;
    private final CompanySuggestionsQuery companySuggestionsQuery;


    public CompanyService(CreateCompanyHandler createCompanyHandler,
                          CreateCompanyContactHandler createCompanyContactHandler,
                          CompanyQueryService companyQueryService, CompanySuggestionsQuery companySuggestionsQuery) {
        this.createCompanyHandler = createCompanyHandler;
        this.createCompanyContactHandler = createCompanyContactHandler;
        this.companyQueryService = companyQueryService;
        this.companySuggestionsQuery = companySuggestionsQuery;
    }

    @Override
    public UUID create(UUID userId, UUID organizationId,
                       String name,
                       String countryCode,
                       String taxId,
                       String registrationNumber,
                       String city,
                       String street,
                       String postalCode) {


        CreateCompanyCommand command = new CreateCompanyCommand(
            name,
            countryCode,
            taxId,
            registrationNumber,
            city,
            street,postalCode
        );


        var company = createCompanyHandler.handle(
                new ExecutionContext(organizationId,userId, "System"),
                command, false);

        return company.value();
    }

    @Override
    public void createContact(UUID userId, UUID organizationId, UUID companyId, String firstName, String lastName, String phone, String email, String jobTitle) {
        CreateCompanyContactCommand command = new CreateCompanyContactCommand(email, phone, firstName, lastName, jobTitle, false);
        createCompanyContactHandler.handle(new ExecutionContext(organizationId, userId, "System"),new CompanyContactCompanyId(companyId), command);
    }

    @Override
    public List<UUID> findAllIds(UUID organizationId, int pageSize) {
        return companyQueryService.findAll(organizationId, new CompanyListQuery("",  PageRequest.of(0, pageSize)))
                .map(CompanyListItem::id).stream().toList();
    }

    @Override
    public boolean exists(UUID organizationId, UUID companyId) {
        return companyQueryService.findOne(new CompanyId(companyId), new CompanyOrganizationId(organizationId)).isPresent();
    }

    @Override
    public List<CompanySuggestion> findCompanySuggestions(UUID organizationId, String search, String countryCode) {
        return companySuggestionsQuery.find(organizationId, search, countryCode);
    }
}
