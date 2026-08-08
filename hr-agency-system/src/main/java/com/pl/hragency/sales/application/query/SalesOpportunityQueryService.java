package com.pl.hragency.sales.application.query;

import com.pl.hragency.sales.application.port.SalesOpportunityRepository;
import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SalesOpportunityQueryService {

    private final SalesOpportunityRepository repository;

    public SalesOpportunityQueryService(
            SalesOpportunityRepository repository
    ) {
        this.repository = repository;
    }

    public Page<SalesOpportunityItem> findAll(
            UUID organizationId,
            SalesOpportunityStage stage,
            Pageable pageable
    ) {



        return repository
                .findAll(organizationId, stage, pageable)
                .map(SalesOpportunityItem::from);
    }

    public Page<SalesOpportunityItem> findByCompany(
            UUID organizationId,
            UUID companyId,
            Pageable pageable
    ) {
        return repository
                .findByCompanyId(
                        organizationId,
                        companyId,
                        pageable
                )
                .map(SalesOpportunityItem::from);
    }
}