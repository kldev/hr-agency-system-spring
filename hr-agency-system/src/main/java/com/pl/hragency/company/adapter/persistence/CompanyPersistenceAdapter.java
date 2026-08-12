package com.pl.hragency.company.adapter.persistence;

import com.pl.hragency.company.application.port.CompanyRepository;

import java.util.Optional;
import java.util.UUID;

import com.pl.hragency.company.domain.model.Company;
import com.pl.hragency.company.domain.model.CompanyId;
import org.springframework.stereotype.Component;

@Component
public class CompanyPersistenceAdapter implements CompanyRepository {
    private final SpringDataCompanyRepository repository;
    private final CompanyMapper mapper;

    public CompanyPersistenceAdapter(SpringDataCompanyRepository repository, CompanyMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Company save(Company company) {
        var record = mapper.toEntity(company);
        return mapper.toDomain(repository.save(record));
    }

    @Override
    public boolean exists(String taxId, UUID organizationId) {
        return repository.existsByOrganizationIdAndTaxId(organizationId, taxId);
    }

    @Override
    public void assignSales(CompanyId companyId, UUID salesUserId) {
        repository.assignSales(companyId.value(), salesUserId);
    }

    @Override
    public Optional<Company> findById(CompanyId companyId, UUID organizationId) {
        return repository.findByIdAndOrganizationId(companyId.value(), organizationId).map(mapper::toDomain);
    }

    @Override
    public boolean existsByOrg(UUID companyId, UUID organizationId) {
        return repository.existsByIdAndOrganizationId(companyId, organizationId);
    }
}
