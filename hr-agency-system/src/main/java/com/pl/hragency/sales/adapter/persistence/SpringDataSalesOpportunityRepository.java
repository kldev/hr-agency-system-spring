package com.pl.hragency.sales.adapter.persistence;


import com.pl.hragency.sales.domain.model.SalesOpportunityStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface SpringDataSalesOpportunityRepository
        extends JpaRepository<SalesOpportunityJpaEntity, UUID>, JpaSpecificationExecutor<SalesOpportunityJpaEntity> {

    @Modifying
    @Transactional
    @Query("""
        update SalesOpportunityJpaEntity o
           set o.stage = :stage,
               o.lostReason = :lostReason
         where o.id = :id
           and o.organizationId = :organizationId
        """)
    int updateStage(
            @Param("organizationId")
            UUID organizationId,

            @Param("id")
            UUID id,

            @Param("stage")
            SalesOpportunityStage stage,

            @Param("lostReason")
            String lostReason
    );
}