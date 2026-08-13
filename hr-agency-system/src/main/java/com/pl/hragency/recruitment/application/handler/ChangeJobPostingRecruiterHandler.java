package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.ChangeJobPostingRecruiterCommand;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.domain.event.JobPostingRecruiterUpdatedEvent;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ChangeJobPostingRecruiterHandler {
    private final JobPostingRepository repository;
    private final IdentityApi  identityApi;
    private final EventPublisher eventPublisher;

    public ChangeJobPostingRecruiterHandler(JobPostingRepository repository,
                                            IdentityApi identityApi,
                                            EventPublisher eventPublisher) {
        this.repository = repository;
        this.identityApi = identityApi;
        this.eventPublisher = eventPublisher;
    }

    public void handle(ExecutionContext context, JobPostingId id, ChangeJobPostingRecruiterCommand command) {

        JobPosting jobPosting = repository.findById(context.organizationId(), id)
                .orElseThrow(() -> new EntityNotFoundException(EntityType.JobPosting, id.value()));

        UserSnapshot newRecruiter = identityApi.findUser(command.recruiterId(),
                context.organizationId()).orElseThrow(() -> new EntityNotFoundException(EntityType.Recruiter, command.recruiterId()));

        UUID oldRecruiterId;
        oldRecruiterId = jobPosting.recruiterId();

        repository.updateRecruiter(context.organizationId(), id, command.recruiterId());

        UserSnapshot oldRecruiter = identityApi.findUser(oldRecruiterId,
                context.organizationId()).orElse(null);

        var event = new JobPostingRecruiterUpdatedEvent(id.value(),
                context.organizationId(),
                oldRecruiter,
                newRecruiter,
                context.userId(),
                context.fullName(),
                Instant.now());
        
        eventPublisher.publish(event);


    }
}
