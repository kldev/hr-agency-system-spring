CREATE TABLE job_postings
(
    id                  UUID         NOT NULL,
    organization_id     UUID         NOT NULL,
    job_description_id  UUID         NOT NULL,
    company_id          UUID         NOT NULL,
    recruiter_id        UUID         NOT NULL,

    title               VARCHAR(255) NOT NULL,
    summary             VARCHAR(1000),
    description         TEXT         NOT NULL,

    responsibilities    JSONB        NOT NULL,
    requirements        JSONB        NOT NULL,
    skills              JSONB        NOT NULL,

    location            VARCHAR(255),
    country_code        VARCHAR(10),

    employment_type     VARCHAR(30)  NOT NULL,
    work_mode           VARCHAR(30)  NOT NULL,

    salary_min          NUMERIC(12,2),
    salary_max          NUMERIC(12,2),
    salary_currency     VARCHAR(3),

    status              VARCHAR(30)  NOT NULL,

    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL,

    version          BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_job_postings
        PRIMARY KEY (id),

    CONSTRAINT fk_job_postings_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations(id),

    CONSTRAINT fk_job_postings_job_description
        FOREIGN KEY (job_description_id)
            REFERENCES job_descriptions(id)
);

CREATE INDEX idx_job_postings_organization
    ON job_postings (organization_id);

CREATE INDEX idx_job_postings_organization_company_id
    ON job_postings (organization_id, company_id);

CREATE INDEX idx_job_postings_organization_status
    ON job_postings (organization_id, status);

CREATE INDEX idx_job_postings_recruiter
    ON job_postings (organization_id, recruiter_id);

CREATE INDEX idx_job_postings_title_summary
    ON job_postings (organization_id, title, summary);