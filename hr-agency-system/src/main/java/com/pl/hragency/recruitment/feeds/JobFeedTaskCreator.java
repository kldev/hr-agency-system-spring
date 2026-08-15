package com.pl.hragency.recruitment.feeds;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobFeedTaskCreator {


    @Scheduled(fixedDelay = 5_000)
    public void createJobFeedTasks() {
        // claim + process
    }
}
