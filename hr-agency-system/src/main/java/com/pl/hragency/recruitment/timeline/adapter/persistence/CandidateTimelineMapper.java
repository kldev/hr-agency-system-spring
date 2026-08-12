package com.pl.hragency.recruitment.timeline.adapter.persistence;

import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineType;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;

@Component
public class CandidateTimelineMapper {

    private final JsonMapper objectMapper;

    public CandidateTimelineMapper(JsonMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public CandidateTimelineEntry toModel(
            CandidateTimelineJpaEntity entity) {

        try {
            JsonNode data = objectMapper.readTree(entity.getData());

            return new CandidateTimelineEntry(
                    entity.getId(),
                    entity.getOrganizationId(),
                    entity.getCandidateId(),
                    CandidateTimelineType.valueOf(entity.getType()),
                    entity.getActorId(),
                    entity.getActorName(),
                    entity.getOccurredAt(),
                    data
            );
        } catch (JacksonException e) {
            throw new IllegalStateException(
                    "Cannot deserialize candidate timeline data",
                    e
            );
        }
    }

    public CandidateTimelineJpaEntity toEntity(
            CandidateTimelineEntry entry) {

        try {
            String data = objectMapper.writeValueAsString(entry.data());

            return new CandidateTimelineJpaEntity(
                    entry.id(),
                    entry.organizationId(),
                    entry.candidateId(),
                    entry.type().name(),
                    entry.actorId(),
                    entry.actorName(),
                    entry.occurredAt(),
                    data
            );
        } catch (JacksonException e) {
            throw new IllegalStateException(
                    "Cannot serialize candidate timeline data",
                    e
            );
        }
    }
}