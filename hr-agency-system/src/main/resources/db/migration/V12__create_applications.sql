CREATE TABLE applications
(
    id               UUID        NOT NULL,
    organization_id  UUID        NOT NULL,

    candidate_id     UUID        NOT NULL,
    job_posting_id   UUID        NOT NULL,

    source            VARCHAR(30) NOT NULL,
    status            VARCHAR(30) NOT NULL,

    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_applications
        PRIMARY KEY (id),

    CONSTRAINT fk_applications_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidates(id),

    CONSTRAINT fk_applications_job_posting
        FOREIGN KEY (job_posting_id)
            REFERENCES job_postings(id),

    CONSTRAINT uk_applications_candidate_job_posting
        UNIQUE (organization_id, job_posting_id, candidate_id)
);

CREATE INDEX idx_applications_organization
    ON applications (organization_id);

CREATE INDEX idx_applications_candidate
    ON applications (organization_id, candidate_id);

CREATE INDEX idx_applications_job_posting
    ON applications (organization_id, job_posting_id);

CREATE INDEX idx_applications_status
    ON applications (organization_id, status);

CREATE INDEX idx_applications_org_created
    ON applications (organization_id, created_at);