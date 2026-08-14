package com.pl.hragency.company.application.query;

import com.pl.hragency.company.adapter.persistence.CompanyContactPersistenceAdapter;
import com.pl.hragency.company.domain.model.CompanyContact;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanyContactQueryService {
    private final CompanyContactPersistenceAdapter  companyContactPersistenceAdapter;


    public CompanyContactQueryService(CompanyContactPersistenceAdapter companyContactPersistenceAdapter) {
        this.companyContactPersistenceAdapter = companyContactPersistenceAdapter;

    }

    public List<CompanyContactItem> findByCompanyId(UUID organizationId, CompanyContactCompanyId companyId){
        return companyContactPersistenceAdapter.findByCompanyId(organizationId, companyId).stream().map(this::toListItem).toList();
    }

    private CompanyContactItem toListItem(
            CompanyContact entity) {

        return new CompanyContactItem(
                entity.id().value(),
                entity.companyId().value(),
                entity.firstName().value(),
                entity.lastName().value(),
                entity.email().value(),
                entity.phone().value(),
                entity.jobTitle().value(),
                entity.primaryContact()
        );
    }
}
