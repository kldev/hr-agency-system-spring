CREATE TABLE platform_users
(
    id              UUID         NOT NULL,
    email           VARCHAR(320) NOT NULL,
    role            VARCHAR(50)  NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    password_hash   VARCHAR(100)  NOT NULL,

    CONSTRAINT pk_platform_users
        PRIMARY KEY (id),

    CONSTRAINT uk_platform_users_email
        UNIQUE (email)
);