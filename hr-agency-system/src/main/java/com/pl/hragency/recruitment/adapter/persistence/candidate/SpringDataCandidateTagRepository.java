package com.pl.hragency.recruitment.adapter.persistence.candidate;

import com.pl.hragency.recruitment.application.query.CandidateTagItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpringDataCandidateTagRepository extends JpaRepository<CandidateTagJpaEntity, CandidateTagJpaId> {
    public void deleteById(CandidateTagJpaId id);

    @Query(
            """
        select new  com.pl.hragency.recruitment.application.query.CandidateTagItem(t.id, t.name, t.category)
                from CandidateTagJpaEntity c
                        inner join TagJpaEntity  t on t.id = c.tagId
                where c.candidateId = :candidateId
                        order by t.name ASC
        """
    )
    List<CandidateTagItem> getListOfTags(UUID candidateId);

    boolean existsByCandidateIdAndTagId(UUID candidateId, UUID tagId);
}
