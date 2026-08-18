package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.UpdateCandidateSummaryCommand;
import com.pl.hragency.recruitment.application.port.CandidateRepository;
import com.pl.hragency.recruitment.domain.model.candidate.Candidate;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateCandidateSummaryHandler {
    private final Logger logger = LoggerFactory.getLogger(UpdateCandidateSummaryHandler.class);
    private final CandidateRepository repository;

    public UpdateCandidateSummaryHandler(CandidateRepository repository) {
        this.repository = repository;
    }

    public void execute(ExecutionContext context, UUID candidateId, UpdateCandidateSummaryCommand command) {
        logger.info("Update candidate summary {} by {}",candidateId, context.userId());

        Candidate candidate = repository.findById(context.organizationId(), new CandidateId( candidateId))
                .orElseThrow(() -> new EntityNotFoundException(EntityType.Candidate, candidateId));

        candidate.updateSummary(command.summary());

        repository.update(candidate);
    }
}
