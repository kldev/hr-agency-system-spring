package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.JobApplicationNoteItem;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationNote;

import java.util.List;
import java.util.UUID;

public interface JobApplicationNoteRepository {
    void save(JobApplicationNote note);

    List<JobApplicationNoteItem> findAll(UUID organizationId, UUID applicationId);
}
