CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE sales_opportunity_activities
(
    id                  UUID         NOT NULL,
    organization_id     UUID         NOT NULL,

    sales_opportunity_id UUID        NOT NULL,

    type                VARCHAR(30)  NOT NULL,

    note             VARCHAR(500) NOT NULL,

    occurred_at         TIMESTAMP WITH TIME ZONE NOT NULL,

    created_at          TIMESTAMP WITH TIME ZONE NOT NULL,

    created_by          UUID,

    CONSTRAINT pk_sales_opportunity_activities
        PRIMARY KEY (id),

    CONSTRAINT fk_sales_opportunity_activities_organization
        FOREIGN KEY (organization_id)
            REFERENCES organizations(id),

    CONSTRAINT fk_sales_opportunity_activities_opportunity
        FOREIGN KEY (sales_opportunity_id)
            REFERENCES sales_opportunities(id),

    CONSTRAINT uq_sales_opportunity_activities_organization_id_id
        UNIQUE (organization_id, id),

    CONSTRAINT ck_sales_opportunity_activities_type
        CHECK (
            type IN (
                     'CALL',
                     'EMAIL',
                     'MEETING',
                     'NOTE',
                     'PRESENTATION',
                     'OTHER'
                )
            )
);

CREATE INDEX idx_sales_opportunity_activities_organization
    ON sales_opportunity_activities (organization_id);

CREATE INDEX idx_sales_opportunity_activities_organization_opportunity
    ON sales_opportunity_activities (
                                     organization_id,
                                     sales_opportunity_id
        );

CREATE INDEX idx_sales_opportunity_activities_opportunity_occurred
    ON sales_opportunity_activities (
                                     sales_opportunity_id,
                                     occurred_at DESC
        );

CREATE INDEX idx_sales_opportunity_activities_organization_type
    ON sales_opportunity_activities (
                                     organization_id,
                                     type
        );

CREATE INDEX idx_sales_opportunity_activities_note_trgm
    ON sales_opportunity_activities
    USING GIN (lower(note) gin_trgm_ops);