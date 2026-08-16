package com.pl.hragency.recruitment.adapter.persistence.interview;

import com.pl.hragency.recruitment.application.port.InterviewRepository;
import com.pl.hragency.recruitment.domain.model.interview.Interview;
import com.pl.hragency.recruitment.domain.model.interview.InterviewId;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

@Component
public class InterviewPersistenceAdapter implements InterviewRepository {
    private final SpringDataInterviewRepository repository;
    private final InterviewMapper mapper;

    public InterviewPersistenceAdapter(SpringDataInterviewRepository repository, InterviewMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(Interview interview) {
        repository.save(mapper.toEntity(interview));
    }

    @Override
    public Optional<Interview> findById(UUID organizationId, InterviewId id) {
        return repository.findByOrganizationIdAndId(organizationId, id.value()).map(mapper::toDomain);
    }

}
