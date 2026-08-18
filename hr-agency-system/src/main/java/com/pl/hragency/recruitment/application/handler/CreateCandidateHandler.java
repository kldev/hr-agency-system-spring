package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.CreateCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.application.service.CandidateResolver;
import com.pl.hragency.recruitment.domain.event.CandidateCreatedEvent;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateEmail;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.event.EventPublisher;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CreateCandidateHandler {

    private final CandidateRepository repository;
    private final CandidateResolver candidateResolver;

    public CreateCandidateHandler(
            CandidateRepository repository,
            CandidateResolver candidateResolver) {

        this.repository = repository;
        this.candidateResolver = candidateResolver;
    }

    @Transactional
    public Candidate handle(
            ExecutionContext context,
            CreateCandidateCommand command) {

        var existing = repository.findByEmail(
                new CandidateEmail(command.email()),
                context.organizationId()
        );

        return existing.orElseGet(() -> candidateResolver.create(
                context,
                command
        ));

    }
}
