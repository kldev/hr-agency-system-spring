package com.pl.hragency.recruitment.feeds;

import com.pl.hragency.recruitment.feeds.port.JobFeedTaskRepository;
import org.springframework.scheduling.annotation.Scheduled;

public class JobFeedTaskWorker {

    private final JobFeedTaskRepository repository;

    public JobFeedTaskWorker(JobFeedTaskRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 30_000)
    public void createJobFeedTasks() {
        // claim + process
    }
}
