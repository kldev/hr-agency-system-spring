package com.pl.hragency.recruitment.feeds.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface SpringDataJobFeedTaskRepository extends JpaRepository<JobFeedTaskJpaEntity, UUID> {
    @Query(
            value = """
        select id,
               organization_id,
               status,
               attempts,
               created_at,
               started_at,
               completed_at,
              error_message
        from  job_feed_tasks where completed_at is null
                                     and status = 'PENDING'
                order by created_at asc
                limit :batchSize
              FOR UPDATE SKIP LOCKED
        """
    , nativeQuery = true)
    List<JobFeedTaskJpaEntity> findPendingForUpdate(int batchSize);

}
