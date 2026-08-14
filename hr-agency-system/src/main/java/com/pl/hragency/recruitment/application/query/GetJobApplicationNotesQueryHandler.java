package com.pl.hragency.recruitment.application.query;

import com.pl.hragency.recruitment.application.port.JobApplicationNoteRepository;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationId;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetJobApplicationNotesQueryHandler {
    private final JobApplicationNoteRepository repository;

    public GetJobApplicationNotesQueryHandler(JobApplicationNoteRepository repository) {
        this.repository = repository;
    }

    public List<JobApplicationNoteItem> getJobApplicationNotes(UUID organizationId,
                                                               @Param("id") JobApplicationId applicationId) {
        return repository.findAll(organizationId, applicationId.value())
                .stream().map(JobApplicationNoteItem::from).toList();
    }
}
