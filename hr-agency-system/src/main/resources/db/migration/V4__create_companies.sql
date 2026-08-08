CREATE TABLE companies
(
    id              UUID         NOT NULL,
    organization_id UUID         NOT NULL,

    name            VARCHAR(255) NOT NULL,
    tax_id          VARCHAR(100),

    country_code    VARCHAR(2),
    city            VARCHAR(100),
    address         VARCHAR(255),
    postal_code     VARCHAR(30),
    registration_number  VARCHAR(30) not null,

    status          VARCHAR(30)  NOT NULL,

    sales_owner_id  UUID,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_companies
        PRIMARY KEY (id),

    CONSTRAINT fk_companies_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations(id),

    CONSTRAINT uq_companies_organization_registration_number
        UNIQUE (organization_id, registration_number),

    CONSTRAINT uq_companies_organization_id_id
        UNIQUE (organization_id, id)
);

CREATE INDEX idx_companies_organization
    ON companies (organization_id);

CREATE INDEX idx_companies_organization_status
    ON companies (organization_id, status);

CREATE INDEX idx_companies_organization_name
    ON companies (organization_id, name);
