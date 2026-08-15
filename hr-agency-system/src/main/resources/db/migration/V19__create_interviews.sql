CREATE TABLE interviews
(
    id              UUID                     NOT NULL,
    organization_id UUID                     NOT NULL,

    candidate_id    UUID                     NOT NULL,
    application_id  UUID                     NOT NULL,

    feedback        VARCHAR(500)             NULL,
    status          VARCHAR(30)              NOT NULL,
    scheduled_at    TIMESTAMP WITH TIME ZONE  NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE  NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE  NOT NULL,
    created_by      UUID                     NOT NULL,

    CONSTRAINT pk_interviews
        PRIMARY KEY (id),

    CONSTRAINT fk_interviews_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidates (id),

    CONSTRAINT fk_interviews_job_application
        FOREIGN KEY (application_id)
            REFERENCES applications (id)
);

CREATE INDEX idx_interviews_organization
    ON interviews (organization_id);

CREATE INDEX idx_interviews_application
    ON interviews (organization_id, application_id);

CREATE INDEX idx_interviews_status
    ON interviews (organization_id, status);

CREATE INDEX idx_interviews_scheduled
    ON interviews (organization_id, scheduled_at);