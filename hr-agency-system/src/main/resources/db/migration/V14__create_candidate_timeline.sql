CREATE TABLE candidate_timeline
(
    id              UUID        NOT NULL,
    organization_id UUID        NOT NULL,
    candidate_id    UUID        NOT NULL,

    type            VARCHAR(50) NOT NULL,
    actor_id        UUID,
    actor_name      VARCHAR(255) NOT NULL,

    occurred_at     TIMESTAMP WITH TIME ZONE NOT NULL,

    data            JSONB       NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX idx_candidate_timeline_candidate
    ON candidate_timeline (
                           organization_id,
                           candidate_id,
                           occurred_at DESC
        );