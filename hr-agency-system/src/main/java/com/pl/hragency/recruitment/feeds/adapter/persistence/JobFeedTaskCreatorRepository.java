package com.pl.hragency.recruitment.feeds.adapter.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.UUID;

@Repository
public class JobFeedTaskCreatorRepository {

    private final JdbcTemplate jdbcTemplate;

    public JobFeedTaskCreatorRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean create(UUID organizationId) {
        String sql = """
            INSERT INTO job_feed_tasks (
                id,
                organization_id,
                status,
                attempts,
                created_at
            )
            VALUES (?, ?, 'PENDING', 0, now())
            ON CONFLICT DO NOTHING
            """;

        int rows = jdbcTemplate.update(
                sql,
                UUID.randomUUID(),
                organizationId
        );

        return rows == 1;
    }

    public int deleteCompletedBefore(Instant before) {
        String sql = """
            DELETE FROM job_feed_tasks
            WHERE status = 'COMPLETED'
              AND completed_at < ?
            """;

        return jdbcTemplate.update(sql,
                ps ->
                        ps.setObject(1, before.atOffset(ZoneOffset.UTC)));
    }
}
