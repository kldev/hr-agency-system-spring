package com.pl.hragency.recruitment.application.service;

import com.pl.hragency.recruitment.application.command.UpdateCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.event.CandidateUpdatedEvent;
import com.pl.hragency.recruitment.domain.exception.CandidateNotFoundException;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UpdateCandidateHandler {
    private final CandidateRepository repository;
    private final EventPublisher eventPublisher;

    public UpdateCandidateHandler(CandidateRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void handle(ExecutionContext context, CandidateId id, UpdateCandidateCommand command) {

        Candidate candidate = repository.findById(context.organizationId(), id)
                .orElseThrow(() -> new CandidateNotFoundException(id.value()));

        candidate.update(command.email(), command.firstName(), command.lastName(), command.phone());

        var event = new CandidateUpdatedEvent(candidate.id().value(),
                context.organizationId(),
                candidate.firstName(),
                candidate.lastName(),
                candidate.email(),
                context.userId(),
                context.fullName(),
                Instant.now()
                );
        eventPublisher.publish(event);
    }
}
