package com.pl.hragency.company.application.port;

import com.pl.hragency.company.domain.model.Company;
import com.pl.hragency.company.domain.model.CompanyId;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {
    Company save(Company company);
    boolean exists(String taxId, UUID organizationId);
    void assignSales(CompanyId companyId, UUID salesUserId);
    Optional<Company> findById(CompanyId companyId);
    boolean existsByOrg(UUID companyId, UUID organizationId);
}
