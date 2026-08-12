package com.pl.hragency.development.scenario.jobposting;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
public class JobPostingDevelopmentRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JobPostingDevelopmentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public UUID createFromJobDescription(
            UUID jobDescriptionId,
            UUID recruiterId) {

        UUID jobPostingId = UUID.randomUUID();

        int rows = jdbcTemplate.update("""
            INSERT INTO job_postings (
                id,
                organization_id,
                job_description_id,
                recruiter_id,
                title,
                summary,
                description,
                responsibilities,
                requirements,
                skills,
                location,
                country_code,
                employment_type,
                work_mode,
                salary_min,
                salary_max,
                salary_currency,
                status,
                created_at,
                updated_at
            )
            SELECT
                :id,
                jd.organization_id,
                jd.id,
                :recruiterId,
                jd.title,
                jd.summary,
                jd.description,
                jd.responsibilities,
                jd.requirements,
                jd.skills,
                jd.location,
                jd.country_code,
                jd.employment_type,
                jd.work_mode,
                jd.salary_min,
                jd.salary_max,
                jd.salary_currency,
                'DRAFT',
                now(),
                now()
            FROM job_descriptions jd
            WHERE jd.id = :jobDescriptionId
            """,
                new MapSqlParameterSource()
                        .addValue("id", jobPostingId)
                        .addValue("recruiterId", recruiterId)
                        .addValue("jobDescriptionId", jobDescriptionId)
        );

        if (rows != 1) {
            throw new IllegalStateException(
                    "Job description not found: " + jobDescriptionId
            );
        }

        return jobPostingId;
    }

    public List<JobDescriptionOption> findTop25JobDescriptions(
            UUID organizationId) {

        return jdbcTemplate.query("""
            SELECT
                jd.id,
                jd.title
            FROM job_descriptions jd
            WHERE jd.organization_id = :organizationId
            ORDER BY jd.created_at DESC
            LIMIT 25
            """,
                new MapSqlParameterSource()
                        .addValue("organizationId", organizationId),
                (rs, rowNum) -> new JobDescriptionOption(
                        rs.getObject("id", UUID.class),
                        rs.getString("title")
                )
        );
    }

    public record JobDescriptionOption(
            UUID id,
            String title
    ) {
    }
}
