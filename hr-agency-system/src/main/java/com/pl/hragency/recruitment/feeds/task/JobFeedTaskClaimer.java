package com.pl.hragency.recruitment.feeds.task;

import com.pl.hragency.recruitment.feeds.domain.model.JobFeedTask;
import com.pl.hragency.recruitment.feeds.port.JobFeedTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobFeedTaskClaimer {

    private final JobFeedTaskRepository repository;

    public JobFeedTaskClaimer(
            JobFeedTaskRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional
    public List<JobFeedTask> claim(int batchSize) {

        var tasks = repository.findPendingForUpdate(batchSize);

        tasks.forEach(JobFeedTask::start);

        repository.batchSave(tasks);

        return tasks;
    }
}