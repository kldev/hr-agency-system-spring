CREATE TABLE candidates
(
    id               UUID         NOT NULL,
    organization_id  UUID         NOT NULL,

    email            VARCHAR(320) NOT NULL,
    email_normalized VARCHAR(320) NOT NULL,

    first_name       VARCHAR(100)  NOT NULL,
    last_name        VARCHAR(100)  NOT NULL,
    phone            VARCHAR(50),

    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL,

    version          BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_candidates
        PRIMARY KEY (id),

    CONSTRAINT uq_candidates_organization_email
        UNIQUE (organization_id, email_normalized)
)