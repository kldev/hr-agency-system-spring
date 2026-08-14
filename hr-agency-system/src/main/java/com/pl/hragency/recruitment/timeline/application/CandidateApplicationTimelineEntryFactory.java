package com.pl.hragency.recruitment.timeline.application;
import java.util.Map;
import java.util.UUID;


import com.pl.hragency.recruitment.domain.event.JobApplicationCreatedEvent;
import com.pl.hragency.recruitment.domain.event.JobApplicationNoteCreatedEvent;
import com.pl.hragency.recruitment.domain.event.JobApplicationStatusChangedEvent;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

@Component
public class CandidateApplicationTimelineEntryFactory {
    private final JsonMapper jsonMapper;

    public CandidateApplicationTimelineEntryFactory(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public CandidateTimelineEntry from(JobApplicationCreatedEvent event){

        var data = jsonMapper.valueToTree(
                Map.of("email", event.email(),
                        "source", event.source()));

        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                CandidateTimelineType.APPLICATION_CREATED,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data
        );
    }

    public CandidateTimelineEntry from(JobApplicationStatusChangedEvent event){

        var data = jsonMapper.valueToTree(
                Map.of("oldStatus", event.oldStatus(),
                        "newStatus", event.newStatus()));

        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                CandidateTimelineType.APPLICATION_STATUS_CHANGED,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data
        );
    }

    public CandidateTimelineEntry from(JobApplicationNoteCreatedEvent event){

        var data = jsonMapper.valueToTree(
                Map.of("note", event.note()));

        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                CandidateTimelineType.APPLICATION_NOTE_ADDED,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data
        );
    }
}
