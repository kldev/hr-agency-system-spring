CREATE TABLE job_feed_tasks
(
    id              UUID PRIMARY KEY,
    organization_id UUID         NOT NULL,

    status          VARCHAR(20)  NOT NULL,

    attempts        INTEGER      NOT NULL DEFAULT 0,

    created_at      TIMESTAMPTZ  NOT NULL,
    started_at      TIMESTAMPTZ,
    completed_at    TIMESTAMPTZ,

    error_message   TEXT
);

CREATE INDEX idx_job_feed_tasks_pending
    ON job_feed_tasks (status, created_at);

CREATE UNIQUE INDEX ux_job_feed_tasks_active
    ON job_feed_tasks (organization_id)
    WHERE status IN ('PENDING', 'PROCESSING');