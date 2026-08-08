CREATE TABLE organizations
(
    id         UUID         NOT NULL,
    name       VARCHAR(200) NOT NULL,
    slug       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_organizations
        PRIMARY KEY (id),

    CONSTRAINT uk_organizations_slug
        UNIQUE (slug)
);