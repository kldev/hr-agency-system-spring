package com.pl.hragency.sales.adapter.persistence;

import com.pl.hragency.sales.application.query.SalesOpportunityActivityItem;
import com.pl.hragency.sales.application.query.SalesOpportunityActivityQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataSalesOpportunityActivityRepository extends JpaRepository<SalesOpportunityActivityJpaEntity, UUID>, JpaSpecificationExecutor<SalesOpportunityActivityJpaEntity> {
    Optional<SalesOpportunityActivityJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);

    @Query("""
    select new com.pl.hragency.sales.application.query.SalesOpportunityActivityItem(
        a.id,
        a.salesOpportunityId,        
        a.type,
        a.note,
        a.occurredAt,
        a.createdAt,
        a.createdBy,
        concat(u.firstName, ' ', u.lastName)
    )
    from SalesOpportunityActivityJpaEntity a
    left join UserJpaEntity u on u.id = a.createdBy
    where a.organizationId = :organizationId
      and (
          :#{#query.salesOpportunityId} is null
          or a.salesOpportunityId = :#{#query.salesOpportunityId}
      )
      and (
          :#{#query.type} is null
          or a.type = :#{#query.type}
      )
      and (
          :#{#query.occurredFrom} is null
          or a.occurredAt >= :#{#query.occurredFrom}
      )
      and (
          :#{#query.occurredTo} is null
          or a.occurredAt <= :#{#query.occurredTo}
      )
      and (
          :#{#query.search} is null
          or lower(a.note) like lower(
              concat('%', :#{#query.search}, '%')
          )
      )
    """)
    Page<SalesOpportunityActivityItem> search(
            @Param("organizationId") UUID organizationId,
            @Param("query") SalesOpportunityActivityQuery query,
            Pageable pageable
    );
}
