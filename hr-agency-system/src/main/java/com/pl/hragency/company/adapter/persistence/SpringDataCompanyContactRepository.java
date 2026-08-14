package com.pl.hragency.company.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataCompanyContactRepository extends JpaRepository<CompanyContactJpaEntity, UUID> {

    @Modifying
    @Query("""
        update CompanyContactJpaEntity  w
            set w.primaryContact = false 
                where w.id != :skipId and w.companyId = :companyId
    """)
    void resetPrimary(UUID skipId, UUID companyId);

    List<CompanyContactJpaEntity> findByOrganizationIdAndCompanyId(UUID organizationId, UUID companyId);
    Optional<CompanyContactJpaEntity> findByOrganizationIdAndId(UUID organizationId, UUID id);
}
