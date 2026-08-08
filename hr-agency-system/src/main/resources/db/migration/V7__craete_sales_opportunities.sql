CREATE TABLE sales_opportunities
(
    id                  UUID         NOT NULL,
    organization_id     UUID         NOT NULL,

    company_id          UUID         NOT NULL,

    title               VARCHAR(255) NOT NULL,
    description         VARCHAR(2000),

    stage               VARCHAR(30)  NOT NULL,

    expected_value      NUMERIC(15, 2),
    currency_code       VARCHAR(3),

    expected_close_date DATE,

    lost_reason         VARCHAR(1000),

    sales_owner_id      UUID,

    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT pk_sales_opportunities
        PRIMARY KEY (id),

    CONSTRAINT fk_sales_opportunities_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations(id),

    CONSTRAINT fk_sales_opportunities_company
        FOREIGN KEY (company_id)
            REFERENCES companies(id),

    CONSTRAINT uq_sales_opportunities_organization_id_id
        UNIQUE (organization_id, id),

    CONSTRAINT ck_sales_opportunities_stage
        CHECK (
            stage IN (
                      'NEW',
                      'QUALIFIED',
                      'PROPOSAL',
                      'WON',
                      'LOST'
                )
            ),

    CONSTRAINT ck_sales_opportunities_expected_value
        CHECK (
            expected_value IS NULL
                OR expected_value >= 0
            )
);

CREATE INDEX idx_sales_opportunities_organization
    ON sales_opportunities (organization_id);

CREATE INDEX idx_sales_opportunities_organization_company
    ON sales_opportunities (organization_id, company_id);

CREATE INDEX idx_sales_opportunities_organization_stage
    ON sales_opportunities (organization_id, stage);

CREATE INDEX idx_sales_opportunities_organization_owner
    ON sales_opportunities (organization_id, sales_owner_id);

CREATE INDEX idx_sales_opportunities_organization_close_date
    ON sales_opportunities (organization_id, expected_close_date);