package com.pl.hragency.recruitment.timeline.application;

import com.pl.hragency.recruitment.domain.event.InterviewScheduledEvent;
import com.pl.hragency.recruitment.domain.event.InterviewStatusChangedEvent;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineEntry;
import com.pl.hragency.recruitment.timeline.model.CandidateTimelineType;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.util.Map;
import java.util.UUID;

@Component
public class CandidateInterviewTimelineEntryFactory {
    private final JsonMapper jsonMapper;

    public CandidateInterviewTimelineEntryFactory(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public CandidateTimelineEntry from(InterviewScheduledEvent event){

        var data = jsonMapper.valueToTree(
                Map.of("date", event.interviewDate()
                        ));

        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                CandidateTimelineType.INTERVIEW_SCHEDULED,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data
        );
    }

    public CandidateTimelineEntry from(InterviewStatusChangedEvent event){

        var data = jsonMapper.valueToTree(
                Map.of("newStatus", event.newStatus()));

        CandidateTimelineType type = null;

        switch (event.newStatus()) {
            case CANCELLED ->  type = CandidateTimelineType.INTERVIEW_CANCELLED;
            case COMPLETED ->  type = CandidateTimelineType.INTERVIEW_COMPLETED;
            case NO_SHOW ->    type = CandidateTimelineType.INTERVIEW_NO_SHOW;
        }

        if (type == null) throw new IllegalArgumentException("newStatus");

        return  new CandidateTimelineEntry(
                UUID.randomUUID(),
                event.candidateId(),
                event.organizationId(),
                type,
                event.actorId(),
                event.actorName(),
                event.occurredOn(),
                data
        );
    }
}
