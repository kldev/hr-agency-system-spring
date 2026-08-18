package com.pl.hragency.recruitment.application.port;

import com.pl.hragency.recruitment.application.query.CandidateTagItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateTagging;

import java.util.List;
import java.util.UUID;

public interface CandidateTaggingRepository {
    public void create(CandidateTagging tagging);
    public void remove(CandidateId candidateId, UUID tagId);
    public List<CandidateTagItem> getListOfTags(CandidateId id);
    public boolean tagExists(CandidateId id, UUID tagId);
}
