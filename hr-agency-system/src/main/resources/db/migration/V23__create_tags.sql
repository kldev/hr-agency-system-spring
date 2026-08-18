CREATE TABLE tags
(
    id         UUID         NOT NULL,
    category   VARCHAR(50)  NOT NULL,
    code       VARCHAR(100) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    active     BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_tags
        PRIMARY KEY (id),

    CONSTRAINT uq_tags_category_code
        UNIQUE (category, code)
);