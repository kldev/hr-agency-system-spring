package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationNote;

import java.time.Instant;
import java.util.UUID;

public record JobApplicationNoteItem(UUID id,
                                     UUID applicationId,
                                     UUID authorId,
                                     String content,
                                     Instant createdAt) {

    public static JobApplicationNoteItem from(JobApplicationNote note) {
        return new JobApplicationNoteItem(note.id().value(),
                note.applicationId(),
                note.authorId(),
                note.content(),
                note.createdAt());
    }
}
