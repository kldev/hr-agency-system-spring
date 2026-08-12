package com.pl.hragency.recruitment.timeline.application;
import com.pl.hragency.recruitment.domain.event.CandidateCreatedEvent;
import com.pl.hragency.recruitment.domain.event.CandidateStatusChangedEvent;
import com.pl.hragency.recruitment.domain.event.CandidateUpdatedEvent;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineType;

import java.util.Map;
import java.util.UUID;


import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class CandidateTimelineEntryFactory {

    private final JsonMapper jsonMapper;

    public CandidateTimelineEntryFactory(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public CandidateTimelineEntry from(CandidateCreatedEvent event){

        var data = jsonMapper.valueToTree(
                Map.of("email", event.email()));

        return  new CandidateTimelineEntry(
                    UUID.randomUUID(),
                    event.candidateId(),
                    event.organizationId(),
                    CandidateTimelineType.CANDIDATE_CREATED,
                    event.actorId(),
                    event.actorName(),
                    event.occurredOn(),
                    data
                );
    }

    public CandidateTimelineEntry from(CandidateUpdatedEvent event){

        var data = jsonMapper.valueToTree(
                        Map.of("email", event.email()));


        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                CandidateTimelineType.CANDIDATE_UPDATED,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data);
    }

    public CandidateTimelineEntry from(CandidateStatusChangedEvent event){

        var data = jsonMapper.valueToTree(
                Map.of(
                        "newStatus", event.newStatus(),
                        "oldStatus", event.oldStatus()
                )
        );

        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                CandidateTimelineType.CANDIDATE_STATUS_CHANGED,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data
        );
    }
}
