CREATE TABLE event_publication
(
    id                     UUID         NOT NULL,
    listener_id            VARCHAR(512) NOT NULL,
    event_type             VARCHAR(512) NOT NULL,
    serialized_event       TEXT         NOT NULL,
    publication_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    completion_date        TIMESTAMP WITH TIME ZONE,
    status                 VARCHAR(20),
    completion_attempts    INTEGER,
    last_resubmission_date TIMESTAMP WITH TIME ZONE,

    CONSTRAINT pk_event_publication
        PRIMARY KEY (id)
);

CREATE INDEX idx_event_publication_listener_event
    ON event_publication (listener_id, serialized_event);

CREATE INDEX idx_event_publication_completion_date
    ON event_publication (completion_date);