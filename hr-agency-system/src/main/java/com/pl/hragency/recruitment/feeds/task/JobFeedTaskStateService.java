package com.pl.hragency.recruitment.feeds.task;

import com.pl.hragency.recruitment.feeds.domain.model.JobFeedTask;
import com.pl.hragency.recruitment.feeds.port.JobFeedTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobFeedTaskStateService {

    private final JobFeedTaskRepository repository;

    public JobFeedTaskStateService(JobFeedTaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void complete(JobFeedTask task) {
        task.complete();
        repository.save(task);
    }

    @Transactional
    public void fail(
            JobFeedTask task,
            String message
    ) {
        task.fail(message);
        repository.save(task);
    }
}
