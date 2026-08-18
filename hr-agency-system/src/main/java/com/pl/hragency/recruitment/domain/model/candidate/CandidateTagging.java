package com.pl.hragency.recruitment.domain.model.candidate;

import java.util.UUID;

public record CandidateTagging(
        UUID candidateId,
        UUID tagId
) {
}
