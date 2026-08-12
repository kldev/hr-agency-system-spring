package com.pl.hragency.company.application.service;

import com.pl.hragency.company.application.command.CreateCompanyContactCommand;
import com.pl.hragency.company.application.port.CompanyContactRepository;
import com.pl.hragency.company.application.port.CompanyRepository;
import com.pl.hragency.company.domain.model.*;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;


@Service
public class CreateCompanyContactHandler {
    private final CompanyContactRepository companyContactRepository;
    private final CompanyRepository companyRepository;
    public CreateCompanyContactHandler(CompanyContactRepository companyContactRepository, CompanyRepository companyRepository) {
        this.companyContactRepository = companyContactRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    public CompanyContactId handle(ExecutionContext context, CompanyContactCompanyId companyId, CreateCompanyContactCommand command) {

        if (!companyRepository.existsByOrg(companyId.value(), context.organizationId())) {
            throw new IllegalArgumentException("Company not belong to your organization");
        }

        CompanyContact companyContact = CompanyContact.create(
                new CompanyContactOrganizationId(context.organizationId()),
                companyId,
                new ContactFirstName(command.firstName()),
                new ContactLastName(command.lastName()),
                new ContactEmail(command.email()),
                new ContactPhone(command.phone()),
                new JobTitle(command.jobTitle()),
                command.primaryContact()
                );
        companyContactRepository.save(companyContact);

        return companyContact.id();
    }
}
