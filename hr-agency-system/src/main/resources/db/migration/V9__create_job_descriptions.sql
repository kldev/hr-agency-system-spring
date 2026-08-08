CREATE TABLE job_descriptions
(
    id                  UUID         NOT NULL,
    organization_id     UUID         NOT NULL,
    company_id          UUID         NOT NULL,

    recruiter_id        UUID,

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

    CONSTRAINT pk_job_descriptions
        PRIMARY KEY (id),

    CONSTRAINT fk_job_descriptions_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations(id),

    CONSTRAINT fk_job_descriptions_company
        FOREIGN KEY (company_id)
            REFERENCES companies(id)
);

CREATE INDEX idx_job_descriptions_organization
    ON job_descriptions (organization_id);

CREATE INDEX idx_job_descriptions_organization_status
    ON job_descriptions (organization_id, status);

CREATE INDEX idx_job_descriptions_company
    ON job_descriptions (organization_id, company_id);

CREATE INDEX idx_job_descriptions_recruiter
    ON job_descriptions (organization_id, recruiter_id);

CREATE INDEX idx_job_descriptions_title
    ON job_descriptions (organization_id, title);