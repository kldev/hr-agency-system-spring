package com.pl.hragency.recruitment.adapter.persistence.application;

import com.pl.hragency.recruitment.application.port.JobApplicationNoteRepository;
import com.pl.hragency.recruitment.application.query.JobApplicationNoteItem;
import com.pl.hragency.recruitment.domain.model.application.JobApplicationNote;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JobApplicationNotePersistenceAdapter implements JobApplicationNoteRepository {
    private final SpringDataJobApplicationNoteRepository repository;
    private final JobApplicationNoteMapper mapper;

    public JobApplicationNotePersistenceAdapter(SpringDataJobApplicationNoteRepository repository, JobApplicationNoteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(JobApplicationNote note) {
        repository.save(mapper.toEntity(note));
    }

    @Override
    public List<JobApplicationNoteItem> findAll(UUID organizationId, UUID applicationId) {
        return repository
                .getNotes(organizationId, applicationId);

    }
}
