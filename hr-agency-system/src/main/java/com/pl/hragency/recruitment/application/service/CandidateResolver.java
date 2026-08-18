package com.pl.hragency.recruitment.application.service;

import com.pl.hragency.recruitment.application.command.CreateCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.event.CandidateCreatedEvent;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateEmail;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CandidateResolver {

    private final CandidateRepository repository;
    private final EventPublisher eventPublisher;

    public CandidateResolver(
            CandidateRepository repository,
            EventPublisher eventPublisher) {

        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public Candidate findOrCreate(
            ExecutionContext context,
            CreateCandidateCommand command) {

        var email = new CandidateEmail(command.email());

        return repository.findByEmail(
                email,
                context.organizationId()
        ).orElseGet(() -> create(
                context,
                command
        ));
    }

    public Candidate create(
            ExecutionContext context,
            CreateCandidateCommand command) {

        var candidate = Candidate.create(
                context.organizationId(),
                command.email(),
                command.firstName(),
                command.lastName(),
                command.phone(),
                command.source()
        );

        repository.create(candidate);

        eventPublisher.publish(
                new CandidateCreatedEvent(
                        candidate.id().value(),
                        candidate.organizationId(),
                        candidate.firstName(),
                        candidate.lastName(),
                        candidate.email(),
                        candidate.source(),
                        context.userId(),
                        context.fullName(),
                        Instant.now()
                )
        );

        return candidate;
    }
}
