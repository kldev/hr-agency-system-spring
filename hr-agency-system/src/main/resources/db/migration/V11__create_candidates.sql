CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE candidates
(
    id               UUID         NOT NULL,
    organization_id  UUID         NOT NULL,

    email            VARCHAR(320) NOT NULL,

    first_name       VARCHAR(100),
    last_name        VARCHAR(100),
    phone            VARCHAR(50),
    summary          TEXT NULL,

    status VARCHAR(20) NOT NULL,

    source VARCHAR(50),

    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NULL,

    version          BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_candidates
        PRIMARY KEY (id),

    CONSTRAINT uq_candidates_organization_email
        UNIQUE (organization_id, email)
);

CREATE INDEX idx_candidates_organization_status
    ON candidates (organization_id, status);

CREATE INDEX idx_candidates_first_name_trgm
    ON candidates
    USING gin (lower(first_name) gin_trgm_ops);

CREATE INDEX idx_candidates_last_name_trgm
    ON candidates
    USING gin (lower(last_name) gin_trgm_ops);

CREATE INDEX idx_candidates_email_trgm
    ON candidates
    USING gin (email gin_trgm_ops);

CREATE INDEX idx_candidates_phone_trgm
    ON candidates
    USING gin (phone gin_trgm_ops);

CREATE INDEX idx_candidates_organization_created_at
    ON candidates (organization_id, created_at DESC);