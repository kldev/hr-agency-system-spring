CREATE TABLE application_notes
(
    id               UUID         NOT NULL,
    organization_id  UUID         NOT NULL,
    application_id   UUID         NOT NULL,
    author_id        UUID         NOT NULL,

    content          TEXT         NOT NULL,

    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_application_notes
        PRIMARY KEY (id),

    CONSTRAINT fk_application_notes_application
        FOREIGN KEY (application_id)
            REFERENCES applications(id),

    CONSTRAINT fk_application_notes_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations(id)
);

CREATE INDEX idx_application_notes_application
    ON application_notes (organization_id, application_id, created_at);

CREATE INDEX idx_application_notes_author
    ON application_notes (organization_id, author_id, created_at);