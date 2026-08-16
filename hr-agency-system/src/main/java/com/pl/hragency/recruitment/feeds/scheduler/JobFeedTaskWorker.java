package com.pl.hragency.recruitment.feeds.scheduler;

import com.pl.hragency.recruitment.feeds.task.JobFeedTaskClaimer;
import com.pl.hragency.recruitment.feeds.task.JobFeedTaskProcessor;
import com.pl.hragency.recruitment.feeds.task.JobFeedTaskStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobFeedTaskWorker {

    private final Logger logger = LoggerFactory.getLogger(JobFeedTaskWorker.class);
    private final JobFeedTaskClaimer claimer;
    private final JobFeedTaskStateService stateService;
    private final JobFeedTaskProcessor processor;

    public JobFeedTaskWorker(JobFeedTaskClaimer claimer,
                             JobFeedTaskStateService stateService,
                             JobFeedTaskProcessor processor) {
        this.claimer = claimer;
        this.stateService = stateService;
        this.processor = processor;
    }

    @Scheduled(fixedDelayString = "${job-feeds.worker.fixed-delay:30s}")
    public void run() {
        this.processBatch();
    }

    private void processBatch() {
        var tasks = claimer.claim(100);

        for (var task : tasks) {
            try {
                processor.process(task);
                stateService.complete(task);

            }
            catch (Exception e) {
                logger.error(e.getMessage());
                stateService.fail(
                        task,
                        errorMessage(e)
                );
            }
        }
    }
    private static String errorMessage(Exception e) {
        return e.getMessage() != null
                ? e.getMessage()
                : e.getClass().getSimpleName();
    }
}
