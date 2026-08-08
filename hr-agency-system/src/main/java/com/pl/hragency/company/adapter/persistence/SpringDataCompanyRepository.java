package com.pl.hragency.company.adapter.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataCompanyRepository extends JpaRepository<CompanyJpaEntity, UUID>, JpaSpecificationExecutor<CompanyJpaEntity> {
    boolean existsByOrganizationIdAndTaxId(UUID organizationId, String taxId);
    boolean existsByIdAndOrganizationId(UUID id, UUID organizationId);

    @Modifying
    @Query("""
        update CompanyJpaEntity w set w.salesOwnerId = :salePersonId where w.id = :companyId
    """)
    void assignSales(UUID companyId, UUID salePersonId);

    Optional<CompanyJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
}
