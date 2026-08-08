package com.pl.hragency.company.adapter.persistence;

import com.pl.hragency.company.domain.model.Company;
import com.pl.hragency.company.domain.model.CompanyId;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {
    public CompanyJpaEntity toEntity(Company company) {

        return new CompanyJpaEntity(
                company.id().value(),
                company.organizationId().value(),
                company.name(),
                company.address().countryCode().value(),
                company.taxId().value(),
                company.address().city(),
                company.address().street(),
                company.address().postalCode(),
                company.registrationNumber().value(),
                company.status(),
                company.salesOwnerId(),
                company.createdAt()
        );
    }

    public Company toDomain(CompanyJpaEntity entity) {

        return Company.rehydrate(
                new CompanyId(entity.getId()),
                entity.getOrganizationId(),
                entity.getName(),
                entity.getTaxId(),
                entity.getCountryCode(),
                entity.getCity(),
                entity.getAddress(),
                entity.getPostalCode(),
                entity.getRegistrationNumber(),
                entity.getStatus(),
                entity.getSalesOwnerId(),
                entity.getCreatedAt()
        );
    }
}
