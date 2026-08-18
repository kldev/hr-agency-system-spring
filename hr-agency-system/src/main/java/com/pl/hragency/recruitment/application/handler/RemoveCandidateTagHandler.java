package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.RemoveCandidateTagCommand;
import com.pl.hragency.recruitment.application.port.CandidateTaggingRepository;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class RemoveCandidateTagHandler {
    private final Logger logger = LoggerFactory.getLogger(RemoveCandidateTagHandler.class);
    private final CandidateTaggingRepository repository;

    public RemoveCandidateTagHandler(CandidateTaggingRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(ExecutionContext context,
                        UUID candidateId,
                        RemoveCandidateTagCommand command) {

        logger.debug("Executing RemoveCandidateTagHandler for candidate id {} by {}", candidateId, context.userId());

        if (!repository.tagExists(new CandidateId(candidateId), command.tagId())){
            throw new EntityNotFoundException(EntityType.CandidateTag, Map.of("tagId", command.tagId(),
                    "candidateId", candidateId));
        }

        repository.remove(new CandidateId(candidateId), command.tagId());

    }
}
