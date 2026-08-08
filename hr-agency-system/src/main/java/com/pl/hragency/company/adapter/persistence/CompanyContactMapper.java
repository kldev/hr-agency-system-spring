package com.pl.hragency.company.adapter.persistence;

import com.pl.hragency.company.domain.model.*;
import org.springframework.stereotype.Component;

@Component
public class CompanyContactMapper {
    public CompanyContactJpaEntity toEntity(CompanyContact companyContact) {
        CompanyContactJpaEntity jpa = new CompanyContactJpaEntity();
        jpa.setId(companyContact.id().value());
        jpa.setOrganizationId(companyContact.organizationId().value());
        jpa.setCompanyId(companyContact.companyId().value());
        jpa.setFirstName(companyContact.firstName().value());
        jpa.setLastName(companyContact.lastName().value());
        jpa.setEmail(companyContact.email().value());
        jpa.setPhone(companyContact.phone().value());
        jpa.setJobTitle(companyContact.jobTitle().value());
        jpa.setPrimaryContact(companyContact.primaryContact());
        jpa.setCreatedAt(companyContact.createdAt());
        return jpa;
    }

    public CompanyContact toDomain(CompanyContactJpaEntity jpa) {
        return CompanyContact.rehydrate(
                new CompanyContactId(jpa.getId()),
                new CompanyContactOrganizationId(jpa.getOrganizationId()),
                new CompanyContactCompanyId(jpa.getCompanyId()),
                new ContactFirstName(jpa.getFirstName()),
                new ContactLastName(jpa.getLastName()),
                new ContactEmail(jpa.getEmail()),
                new ContactPhone(jpa.getPhone()),
                new JobTitle(jpa.getJobTitle()),
                jpa.getPrimaryContact(),
                jpa.getCreatedAt()
        );
    }
}
