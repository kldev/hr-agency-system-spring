package com.pl.hragency.company.application.port;

import com.pl.hragency.company.domain.model.CompanyContact;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.company.domain.model.CompanyContactId;

import java.util.List;
import java.util.Optional;

public interface CompanyContactRepository {
    CompanyContact save(CompanyContact companyContact);
    Optional<CompanyContact> findById(CompanyContactId id);
    List<CompanyContact> findByCompanyId(CompanyContactCompanyId companyId);
}
