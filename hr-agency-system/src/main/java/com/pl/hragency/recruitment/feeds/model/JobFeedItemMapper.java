package com.pl.hragency.recruitment.feeds.model;

import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JobFeedItemMapper {
    private final String appUrl;

    public JobFeedItemMapper(@Value("${app.base.url:http://localhost:8080}") String appUrl) {
        this.appUrl = appUrl;
    }

    public JobFeedItem toItem(JobPosting posting) {
        return new JobFeedItem(
                posting.id().value(),
                posting.title(),
                posting.summary(),
                posting.description(),
                appUrl + "/public/" + posting.organizationSlug() + "/apply/" + posting.slug(),
                posting.responsibilities(),
                posting.requirements(),
                posting.skills(),
                posting.location(),
                posting.countryCode(),
                posting.employmentType(),
                posting.workMode(),
                posting.salaryRange(),
                posting.createdAt()
        );
    }
}