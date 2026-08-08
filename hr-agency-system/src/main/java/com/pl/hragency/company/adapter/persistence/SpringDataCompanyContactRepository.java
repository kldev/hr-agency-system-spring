package com.pl.hragency.company.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataCompanyContactRepository extends JpaRepository<CompanyContactJpaEntity, UUID> {

    @Modifying
    @Query("""
        update CompanyContactJpaEntity  w
            set w.primaryContact = false 
                where w.id != :skipId and w.companyId = :companyId
    """)
    void resetPrimary(UUID skipId, UUID companyId);

    List<CompanyContactJpaEntity> findByCompanyId(UUID companyId);
}
