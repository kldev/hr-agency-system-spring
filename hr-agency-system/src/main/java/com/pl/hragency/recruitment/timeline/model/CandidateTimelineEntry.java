package com.pl.hragency.recruitment.timeline.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(
                type = "object",
                additionalProperties = Schema.AdditionalPropertiesValue.TRUE,
                description = "Event-specific timeline data"
        )
        JsonNode data
) {
}
