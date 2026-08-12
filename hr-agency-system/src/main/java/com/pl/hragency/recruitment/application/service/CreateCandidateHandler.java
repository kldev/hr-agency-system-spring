package com.pl.hragency.recruitment.application.service;

import com.pl.hragency.recruitment.application.command.CreateCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.event.CandidateCreatedEvent;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateCandidateHandler {
    private final CandidateRepository repository;
    private final EventPublisher eventPublisher;

    public CreateCandidateHandler(CandidateRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }


    @Transactional
    public CandidateId handle(ExecutionContext context, CreateCandidateCommand command) {
        var exists = repository.existsByEmail(command.email(), context.organizationId());

        if (exists) {
            throw new IllegalArgumentException("Email already exists!");
        }

        Candidate candidate = Candidate.create(context.organizationId(),
                command.email(),
                command.firstName(),
                command.lastName(),
                command.phone(),
                command.source());

        repository.save(candidate);

        var event = new CandidateCreatedEvent(candidate.id().value(),
                candidate.organizationId(),
                candidate.firstName(),
                candidate.lastName(),
                candidate.email(),
                candidate.source(),
                context.userId(),
                context.fullName(),
                Instant.now());

        eventPublisher.publish(event);

        return candidate.id();
    }
}
