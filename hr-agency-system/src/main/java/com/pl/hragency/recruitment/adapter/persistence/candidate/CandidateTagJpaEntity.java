package com.pl.hragency.recruitment.adapter.persistence.candidate;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "candidate_tags"
)
@IdClass(CandidateTagJpaId.class)
public class CandidateTagJpaEntity {

    @Id
    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Id
    @Column(name = "tag_id", nullable = false)
    private UUID tagId;

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    protected CandidateTagJpaEntity() {
    }

    public CandidateTagJpaEntity(
            CandidateTagJpaId id
    ) {
        this.candidateId = id.candidateId();
        this.tagId = id.tagId();
        this.createdAt = Instant.now();
    }


    public UUID getCandidateId() {
        return candidateId;
    }

    public UUID getTagId() {
        return tagId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
