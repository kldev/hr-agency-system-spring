package com.pl.hragency.recruitment.adapter.persistence.candidate;

import java.util.UUID;

public record CandidateTagJpaId(
        UUID candidateId,
        UUID tagId
) {
}
