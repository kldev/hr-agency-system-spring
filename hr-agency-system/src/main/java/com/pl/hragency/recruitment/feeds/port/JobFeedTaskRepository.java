package com.pl.hragency.recruitment.feeds.port;

import com.pl.hragency.recruitment.feeds.domain.model.JobFeedTask;

import java.time.Instant;
import java.util.List;

public interface JobFeedTaskRepository {

    void save(JobFeedTask jobFeedTask);

    List<JobFeedTask> findPendingForUpdate(int batchSize);

    void batchSave(List<JobFeedTask> jobFeedTasks);
}
