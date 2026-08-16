package com.pl.hragency.recruitment.feeds.scheduler;

import com.pl.hragency.organization.api.OrganizationApi;
import com.pl.hragency.recruitment.feeds.adapter.persistence.JobFeedTaskCreatorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class JobFeedTaskCreator {
    private final Logger logger = LoggerFactory.getLogger(JobFeedTaskCreator.class);
    private final JobFeedTaskCreatorRepository creatorRepository;
    private final OrganizationApi organizationApi;

    public JobFeedTaskCreator(JobFeedTaskCreatorRepository creatorRepository,
                              OrganizationApi organizationApi) {
        this.creatorRepository = creatorRepository;
        this.organizationApi = organizationApi;
    }

    @Scheduled(fixedDelayString = "${job-feeds.creator.fixed-delay:2m}")
    public void createJobFeedTasks() {

        var organizations = organizationApi.findAllActive();
        logger.info("Creating job feed tasks for organizations {} count", organizations.size());
        AtomicInteger counter = new AtomicInteger();
        organizations.forEach(org->{
           if (creatorRepository.create(org.id()))
               counter.getAndIncrement();
        });

        logger.info("Created job feed tasks for organizations {} count", counter);
    }

    //@Transactional
    @Scheduled(cron = "${job-feeds.cleanup.cron:0 0 3 * * *}")
    public void cleanup() {
        logger.info("Clean up completed job feed tasks");
        creatorRepository.deleteCompletedBefore(
                Instant.now().minus(14, ChronoUnit.DAYS)
        );
    }
}
