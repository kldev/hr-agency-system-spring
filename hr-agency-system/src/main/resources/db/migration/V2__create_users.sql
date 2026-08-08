CREATE TABLE users
(
    id              UUID         NOT NULL,
    organization_id UUID         NOT NULL,
    email           VARCHAR(320) NOT NULL,
    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    password_hash   VARCHAR(100)  NOT NULL,

    CONSTRAINT pk_users
        PRIMARY KEY (id),

    CONSTRAINT uk_users_organization_email
        UNIQUE (organization_id, email),

    CONSTRAINT fk_users_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
);