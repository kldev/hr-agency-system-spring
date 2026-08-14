package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.domain.model.application.JobApplicationNote;

import java.time.Instant;
import java.util.UUID;

public record JobApplicationNoteItem(UUID id,
                                     UUID applicationId,
                                     UUID authorId,
                                     String content,
                                     String authorName,
                                     Instant createdAt) {
}
