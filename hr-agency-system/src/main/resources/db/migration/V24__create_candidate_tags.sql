CREATE TABLE candidate_tags
(
    candidate_id UUID        NOT NULL,
    tag_id       UUID        NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL,

    CONSTRAINT pk_candidate_tags
        PRIMARY KEY (candidate_id, tag_id),

    CONSTRAINT fk_candidate_tags_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidates(id),

    CONSTRAINT fk_candidate_tags_tag
        FOREIGN KEY (tag_id)
            REFERENCES tags(id)
);

CREATE INDEX idx_candidate_tags_tag_candidate
    ON candidate_tags (tag_id, candidate_id);