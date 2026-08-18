package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.application.port.CandidateTaggingRepository;
import com.pl.hragency.recruitment.application.query.CandidateTagItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateTagging;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;


@Component
public class CandidateTaggingPersistenceAdapter implements CandidateTaggingRepository {
    private final SpringDataCandidateTagRepository repository;

    public CandidateTaggingPersistenceAdapter(SpringDataCandidateTagRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(CandidateTagging tagging) {

        var id = new CandidateTagJpaId(tagging.candidateId(), tagging.tagId());
        repository.save(new CandidateTagJpaEntity(id));

    }

    @Override
    public void remove(CandidateId candidateId, UUID tagId) {

        var id = new CandidateTagJpaId(candidateId.value(), tagId);
        repository.deleteById(id);

    }

    @Override
    public List<CandidateTagItem> getListOfTags(CandidateId id) {
        return repository.getListOfTags(id.value());
    }

    @Override
    public boolean tagExists(CandidateId candidateId, UUID tagId) {
        return repository.existsByCandidateIdAndTagId(candidateId.value(), tagId);
    }
}
