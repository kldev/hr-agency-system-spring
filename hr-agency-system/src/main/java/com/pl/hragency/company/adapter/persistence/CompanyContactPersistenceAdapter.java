package com.pl.hragency.company.adapter.persistence;

import com.pl.hragency.company.application.port.CompanyContactRepository;
import com.pl.hragency.company.domain.model.CompanyContact;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.company.domain.model.CompanyContactId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CompanyContactPersistenceAdapter implements CompanyContactRepository {
    private final SpringDataCompanyContactRepository  companyContactRepository;
    private final CompanyContactMapper mapper;

    public CompanyContactPersistenceAdapter(SpringDataCompanyContactRepository companyContactRepository, CompanyContactMapper mapper) {
        this.companyContactRepository = companyContactRepository;
        this.mapper = mapper;
    }

    @Override
    public CompanyContact save(CompanyContact companyContact) {

        CompanyContactJpaEntity jpa = mapper.toEntity(companyContact);
        if (companyContact.primaryContact()) {
            companyContactRepository.resetPrimary(jpa.getId(), jpa.getCompanyId());
        }

        companyContactRepository.save(jpa);

        return mapper.toDomain(jpa);
    }

    @Override
    public Optional<CompanyContact> findById(UUID organizationId, CompanyContactId id) {
        return companyContactRepository.findByOrganizationIdAndId(organizationId, id.value()).map(mapper::toDomain);
    }

    @Override
    public List<CompanyContact> findByCompanyId(UUID organizationId, CompanyContactCompanyId companyId) {
        return companyContactRepository.findByOrganizationIdAndCompanyId(organizationId, companyId.value()).stream().map(mapper::toDomain).toList();
    }
}
