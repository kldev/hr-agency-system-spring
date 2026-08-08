CREATE TABLE audit_entries
(
    id              UUID PRIMARY KEY,
    module          VARCHAR(50)  NOT NULL,
    aggregate_type  VARCHAR(100) NOT NULL,
    aggregate_id    UUID         NOT NULL,
    event_type      VARCHAR(50)  NOT NULL,
    actor_id        UUID,
    actor_name      VARCHAR(255),
    description     VARCHAR(1000),
    data            JSONB,
    occurred_at     TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_audit_aggregate
    ON audit_entries (aggregate_type, aggregate_id);

CREATE INDEX idx_audit_occurred_at
    ON audit_entries (occurred_at);

CREATE INDEX idx_audit_actor
    ON audit_entries (actor_id);