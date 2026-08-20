package com.pl.hragency.recruitment.application.query;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobPostingItemMapper {
    private final String appUrl;

    public JobPostingItemMapper(@Value("${app.base.url:http://localhost:8080}") String appUrl) {
        this.appUrl = appUrl;
    }

    public JobPostingItem from(
            JobPosting posting
    ) {
        return new JobPostingItem(
                posting.id().value(),
                posting.organizationId(),
                posting.jobDescriptionId(),
                posting.organizationSlug(),
                posting.title(),
                posting.summary(),
                posting.description(),

                List.copyOf(posting.responsibilities()),
                List.copyOf(posting.requirements()),
                List.copyOf(posting.skills()),

                posting.location(),
                posting.countryCode(),

                posting.employmentType(),
                posting.workMode(),

                posting.salaryRange().min(),
                posting.salaryRange().max(),
                posting.salaryRange().currency().getCurrencyCode(),
                posting.slug(),
                appUrl + "/public/" + posting.organizationSlug() + "/apply/" + posting.slug()
        );
    }
}
