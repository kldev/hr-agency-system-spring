package com.pl.hragency.company.application.query;

import com.pl.hragency.company.domain.model.CompanyId;
import com.pl.hragency.company.domain.model.CompanyOrganizationId;
import org.springframework.stereotype.Service;

import com.pl.hragency.company.adapter.persistence.CompanyJpaEntity;
import com.pl.hragency.company.adapter.persistence.CompanySpecifications;
import com.pl.hragency.company.adapter.persistence.SpringDataCompanyRepository;
import com.pl.hragency.identity.api.IdentityApi;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyQueryService {

    private final SpringDataCompanyRepository repository;


    public CompanyQueryService(
            SpringDataCompanyRepository repository) {

        this.repository = repository;

    }

    public Page<CompanyListItem> findAll(UUID organizationId,
            CompanyListQuery query) {

        Specification<CompanyJpaEntity> specification =
                Specification
                        .where(
                                CompanySpecifications
                                        .organizationId(organizationId)
                        )
                        .and(
                                CompanySpecifications
                                        .search(query.search())
                        );


        return repository
                .findAll(
                        specification,
                        query.pageable()
                )
                .map(this::toListItem);
    }

    public Optional<CompanyListItem> findOne(CompanyId id, CompanyOrganizationId  organizationId)
    {

        return repository
                .findByIdAndOrganizationId(id.value(), organizationId.value())
                .map(this::toListItem);
    }



    private CompanyListItem toListItem(
            CompanyJpaEntity entity) {

        return new CompanyListItem(
                entity.getId(),
                entity.getName(),
                entity.getTaxId(),
                entity.getRegistrationNumber(),
                entity.getCountryCode(),
                entity.getCity(),
                entity.getStatus().toString(),
                entity.getSalesOwnerId()
        );
    }
}
