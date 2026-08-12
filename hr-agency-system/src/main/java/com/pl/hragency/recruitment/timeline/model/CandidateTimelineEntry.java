package com.pl.hragency.recruitment.timeline.model;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.Instant;
import java.util.UUID;

public record CandidateTimelineEntry(
        UUID id,
        UUID candidateId,
        UUID organizationId,
        CandidateTimelineType type,
        UUID actorId,
        String actorName,
        Instant occurredAt,
        JsonNode data
) {
}
