CREATE TABLE company_contacts
(
    id              UUID         NOT NULL,
    organization_id UUID         NOT NULL,
    company_id      UUID         NOT NULL,

    first_name      VARCHAR(100) NOT NULL,
    last_name       VARCHAR(100) NOT NULL,

    email           VARCHAR(320),
    phone           VARCHAR(50),

    job_title       VARCHAR(150),

    primary_contact BOOLEAN      NOT NULL DEFAULT FALSE,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_company_contacts
        PRIMARY KEY (id),

    CONSTRAINT fk_company_contacts_company
        FOREIGN KEY (organization_id, company_id)
            REFERENCES companies (organization_id, id)
);

CREATE INDEX idx_company_contacts_company
    ON company_contacts (
                         organization_id,
                         company_id
        );