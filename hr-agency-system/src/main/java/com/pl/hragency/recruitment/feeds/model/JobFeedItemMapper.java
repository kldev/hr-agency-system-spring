package com.pl.hragency.recruitment.feeds.model;

import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import org.springframework.stereotype.Component;

@Component
public class JobFeedItemMapper {

    public JobFeedItem toItem(JobPosting posting) {
        return new JobFeedItem(
                posting.id().value(),
                posting.title(),
                posting.summary(),
                posting.description(),
                posting.responsibilities(),
                posting.requirements(),
                posting.skills(),
                posting.location(),
                posting.countryCode(),
                posting.employmentType(),
                posting.workMode(),
                posting.salaryRange(),
                posting.createdAt(),
                posting.updatedAt()
        );
    }
}