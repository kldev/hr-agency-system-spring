package com.pl.hragency.recruitment.application.handler;

import com.pl.hragency.recruitment.application.command.TagCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateTaggingRepository;
import com.pl.hragency.recruitment.application.port.TagQueryRepository;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateTagging;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import com.pl.hragency.shared.rest.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TagCandidateHandler {
    private final Logger logger = LoggerFactory.getLogger(TagCandidateHandler.class);
    private final CandidateTaggingRepository repository;
    private final TagQueryRepository queryRepository;

    public TagCandidateHandler(CandidateTaggingRepository repository, TagQueryRepository queryRepository) {
        this.repository = repository;
        this.queryRepository = queryRepository;
    }

    @Transactional
    public void execute(ExecutionContext context, UUID candidateId, TagCandidateCommand command)
    {
        logger.debug("Executing TagCandidateHandler for candidate id {} by {}", candidateId, context.userId());

        queryRepository.getTagById(command.tagId())
                        .orElseThrow(() -> new EntityNotFoundException(EntityType.Tag, command.tagId()));

        if (repository.tagExists(new CandidateId(candidateId), command.tagId())){
            return;
        }

        repository.create(new CandidateTagging(candidateId, command.tagId()));
    }

}
