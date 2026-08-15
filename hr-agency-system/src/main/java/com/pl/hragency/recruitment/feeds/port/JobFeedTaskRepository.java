package com.pl.hragency.recruitment.feeds.port;

import com.pl.hragency.recruitment.feeds.model.JobFeedTask;

import java.util.List;

public interface JobFeedTaskRepository {
    List<JobFeedTask> getJobFeedTasks(int batchSize);
}
