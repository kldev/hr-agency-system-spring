package com.pl.hragency.development.scenario.jobposting;
import com.pl.hragency.identity.api.IdentityApi;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
public class JobPostingScenario {

    private final JobPostingDevelopmentRepository jobPostingDevelopmentRepository;
    private final IdentityApi identityApi;

    public JobPostingScenario(JobPostingDevelopmentRepository jobPostingDevelopmentRepository,
                              IdentityApi identityApi) {
        this.jobPostingDevelopmentRepository = jobPostingDevelopmentRepository;
        this.identityApi = identityApi;
    }

    public void create(
            UUID organizationId
          ) {
        var jobDescriptions = jobPostingDevelopmentRepository.findTop25JobDescriptions(organizationId);
        var recruiterIds = identityApi.findUserSuggestions(organizationId, "", Set.of("RECRUITER"));

        int index = 0;
        UUID userId;

        for (var jobDescription : jobDescriptions) {
            userId = recruiterIds.get(++index % recruiterIds.size()).id();
            jobPostingDevelopmentRepository.createFromJobDescription(jobDescription.id(), userId);
        }
    }
}
