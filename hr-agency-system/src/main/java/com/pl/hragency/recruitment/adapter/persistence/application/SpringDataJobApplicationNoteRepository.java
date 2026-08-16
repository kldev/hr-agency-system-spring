package com.pl.hragency.recruitment.adapter.persistence.application;


import com.pl.hragency.recruitment.application.query.JobApplicationNoteItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataJobApplicationNoteRepository extends JpaRepository<JobApplicationNoteJpaEntity, UUID> {
    Optional<JobApplicationNoteJpaEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);

    @Query("""
    select new com.pl.hragency.recruitment.application.query.JobApplicationNoteItem(
            a.id,
            a.applicationId,
            a.authorId,
            a.content,
            concat(u.firstName, ' ', u.lastName),
            a.createdAt
        ) from JobApplicationNoteJpaEntity a
          inner join UserJpaEntity u on u.id = a.authorId
          where a.applicationId = :applicationId
              and a.organizationId = :organizationId
          order by a.createdAt DESC
    """)
    List<JobApplicationNoteItem> getNotes(UUID organizationId, UUID applicationId);
}
