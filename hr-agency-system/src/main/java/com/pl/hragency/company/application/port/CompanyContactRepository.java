package com.pl.hragency.company.application.port;

import com.pl.hragency.company.domain.model.CompanyContact;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.company.domain.model.CompanyContactId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CompanyContactRepository {
    CompanyContact save(CompanyContact companyContact);
    Optional<CompanyContact> findById(UUID organizationId, CompanyContactId id);
    List<CompanyContact> findByCompanyId(UUID organizationId, CompanyContactCompanyId companyId);
}
