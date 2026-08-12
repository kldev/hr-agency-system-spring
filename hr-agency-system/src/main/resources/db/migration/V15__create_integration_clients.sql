CREATE TABLE integration_clients
(
    id               UUID         NOT NULL,
    organization_id  UUID         NOT NULL,

    name             VARCHAR(150) NOT NULL,
    description      VARCHAR(500),

    api_key_hash     VARCHAR(255) NOT NULL,

    revoked_at TIMESTAMP WITH TIME ZONE,

    created_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_used_at     TIMESTAMP WITH TIME ZONE,

    version          BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT pk_integration_clients
        PRIMARY KEY (id),

    CONSTRAINT uq_integration_clients_api_key_hash
        UNIQUE (api_key_hash),

    CONSTRAINT fk_integration_clients_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
);

CREATE INDEX idx_integration_clients_organization
    ON integration_clients (organization_id);

CREATE INDEX idx_integration_clients_organization_revoked_at
    ON integration_clients (organization_id, revoked_at);

CREATE TABLE integration_client_scopes
(
    integration_client_id UUID         NOT NULL,
    scope                 VARCHAR(100) NOT NULL,

    CONSTRAINT pk_integration_client_scopes
        PRIMARY KEY (integration_client_id, scope),

    CONSTRAINT fk_integration_client_scopes_client
        FOREIGN KEY (integration_client_id)
            REFERENCES integration_clients (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_integration_client_scopes_scope
    ON integration_client_scopes (scope);