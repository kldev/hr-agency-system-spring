ALTER TABLE job_postings
ADD slug VARCHAR(500) NULL;

CREATE UNIQUE INDEX  idx_job_postings_organization_id_slug
    ON job_postings (organization_id, slug);