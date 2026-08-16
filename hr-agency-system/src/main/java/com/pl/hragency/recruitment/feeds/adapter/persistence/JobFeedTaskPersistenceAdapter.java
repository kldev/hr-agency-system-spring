package com.pl.hragency.recruitment.feeds.adapter.persistence;
import com.pl.hragency.recruitment.feeds.domain.model.JobFeedTask;
import com.pl.hragency.recruitment.feeds.port.JobFeedTaskRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class JobFeedTaskPersistenceAdapter implements JobFeedTaskRepository {
    private final SpringDataJobFeedTaskRepository repository;
    private final JobFeedTaskMapper mapper;

    public JobFeedTaskPersistenceAdapter(SpringDataJobFeedTaskRepository repository, JobFeedTaskMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(JobFeedTask jobFeedTask) {
        repository.save(mapper.toEntity(jobFeedTask));
    }

    @Override
    public List<JobFeedTask> findPendingForUpdate(int batchSize) {
        return repository.findPendingForUpdate(batchSize).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public void batchSave(List<JobFeedTask> jobFeedTasks) {
        var entities = jobFeedTasks.stream()
                .map(mapper::toEntity)
                .toList();

        repository.saveAll(entities);
    }

}
