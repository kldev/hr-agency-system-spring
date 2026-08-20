package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.BaseIntegrationTest;
import com.pl.hragency.recruitment.application.port.CandidateQueryRepository;
import com.pl.hragency.recruitment.application.port.CandidateTaggingRepository;
import com.pl.hragency.recruitment.application.query.CandidateListQuery;
import com.pl.hragency.recruitment.application.query.CandidateItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateStatus;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateTagging;
import com.pl.hragency.testsupport.TestCandidateScenario;
import com.pl.hragency.testsupport.TestJobApplicationScenario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CandidateQueryRepositoryTest extends BaseIntegrationTest {

    private static final UUID JAVA_TAG =
            UUID.fromString("20000000-0000-0000-0000-000000000001");

    private static final UUID C_SHARP_TAG =
            UUID.fromString("20000000-0000-0000-0000-000000000002");

    private static final UUID JAVASCRIPT_TAG =
            UUID.fromString("20000000-0000-0000-0000-000000000003");

    private static final UUID TYPESCRIPT_TAG =
            UUID.fromString("20000000-0000-0000-0000-000000000004");

    private static final UUID DOCKER_TAG =
            UUID.fromString("20000000-0000-0000-0000-000000000011");

    @Autowired
    private TestCandidateScenario candidateScenario;

    @Autowired
    private TestJobApplicationScenario  jobApplicationScenario;

    @Autowired
    private CandidateTaggingRepository candidateTaggingRepository;

    @Autowired
    private CandidateQueryRepository repository;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 20);
    }

    @Test
    void shouldReturnAllCandidatesWhenNoFiltersAreSpecified() {
        var javaCandidate = candidateScenario.createCandidate(
                "Jan",
                "Java",
                "jan.java@example.com", null
        );

        var cSharpCandidate = candidateScenario.createCandidate(javaCandidate.organization(), javaCandidate.recruiter(),
                "Adam",
                "CSharp",
                "adam.csharp@example.com",null
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                javaCandidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactlyInAnyOrder(
                        javaCandidate.id(),
                        cSharpCandidate.id()
                );
    }

    @Test
    void shouldFilterBySearch() {
        var javaCandidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        candidateScenario.createCandidate(javaCandidate.organization(), javaCandidate.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com", null
        );

        var query = new CandidateListQuery(
                "kowalski",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                javaCandidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(javaCandidate.candidate().id());
    }

    @Test
    void shouldFilterByEmailUsingSearch() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        candidateScenario.createCandidate(candidate.organization(), candidate.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com", null
        );

        var query = new CandidateListQuery(
                "jan.kowalski",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(candidate.id());
    }

    @Test
    void shouldFilterByFirstNameUsingSearch() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        candidateScenario.createCandidate(candidate.organization(), candidate.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com", null
        );

        var query = new CandidateListQuery(
                "jan",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(candidate.id());
    }

    @Test
    void shouldFilterByLastNameUsingSearch() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        candidateScenario.createCandidate(candidate.organization(), candidate.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com", null
        );

        var query = new CandidateListQuery(
                "kow",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(candidate.id());
    }

    @Test
    void shouldFilterByPhoneUsingSearch() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com",
                "501123456"
        );

        candidateScenario.createCandidate(
                "Adam",
                "Nowak",
                "adam.nowak@example.com",
                "502987654"
        );

        var query = new CandidateListQuery(
                "1123",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(candidate.id());
    }

    @Test
    void shouldFilterByStatus() {
        var activeCandidate = candidateScenario.createCandidate(
                "Jan",
                "Active",
                "jan.active@example.com", null
        );

        var archivedCandidate = candidateScenario.createCandidate(activeCandidate.organization(), activeCandidate.recruiter(),
                "Adam",
                "Archived",
                "adam.archived@example.com", null
        );

        candidateScenario.changeStatus(
                archivedCandidate.organizationId(),
                archivedCandidate.id(),
                CandidateStatus.ARCHIVED
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(),
                CandidateStatus.ACTIVE
        );

        var result = repository.search(
                activeCandidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .contains(activeCandidate.id());

        assertThat(result)
                .extracting(CandidateItem::id)
                .doesNotContain(archivedCandidate.id());
    }

    @Test
    void shouldFilterBySingleTag() {
        var javaCandidate = candidateScenario.createCandidate(
                "Jan",
                "Java",
                "jan.java@example.com", null
        );

        var cSharpCandidate = candidateScenario.createCandidate(javaCandidate.organization(), javaCandidate.recruiter(),
                "Adam",
                "CSharp",
                "adam.csharp@example.com", null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        javaCandidate.id(),
                        JAVA_TAG
                )
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        cSharpCandidate.id(),
                        C_SHARP_TAG
                )
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(JAVA_TAG),
                null
        );

        var result = repository.search(
                javaCandidate.organization().id(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(javaCandidate.candidate().id());
    }

    @Test
    void shouldFilterByAnyOfSpecifiedTags() {
        var javaCandidate = candidateScenario.createCandidate(
                "Jan",
                "Java",
                "jan.java@example.com", null
        );

        var dockerCandidate = candidateScenario.createCandidate(javaCandidate.organization(), javaCandidate.recruiter(),
                "Adam",
                "Docker",
                "adam.docker@example.com", null
        );

        var pythonCandidate = candidateScenario.createCandidate(javaCandidate.organization(), javaCandidate.recruiter(),
                "Piotr",
                "Python",
                "piotr.python@example.com", null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        javaCandidate.candidate().id(),
                        JAVA_TAG
                )
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        dockerCandidate.candidate().id(),
                        DOCKER_TAG
                )
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(JAVA_TAG, DOCKER_TAG),
                null
        );

        var result = repository.search(
                javaCandidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactlyInAnyOrder(
                        javaCandidate.id(),
                        dockerCandidate.id()
                );

        assertThat(result)
                .extracting(CandidateItem::id)
                .doesNotContain(pythonCandidate.id());
    }

    @Test
    void shouldReturnCandidateWithMultipleTagsWhenFilteringByOneOfThem() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "FullStack",
                "jan.fullstack@example.com", null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        candidate.id(),
                        JAVA_TAG
                )
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        candidate.id(),
                        DOCKER_TAG
                )
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(JAVA_TAG),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(candidate.id());
    }

    @Test
    void shouldNotReturnDuplicateCandidateWhenCandidateHasMultipleMatchingTags() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "FullStack",
                "jan.fullstack@example.com", null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        candidate.id(),
                        JAVA_TAG
                )
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        candidate.id(),
                        DOCKER_TAG
                )
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(JAVA_TAG, DOCKER_TAG),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(candidate.id());

        assertThat(result.hasNext())
                .isEqualTo(false);
    }



    @Test
    void shouldCombineSearchAndStatusFilters() {
        var matching = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        var wrongStatus = candidateScenario.createCandidate(matching.organization(), matching.recruiter(),
                "Jan",
                "Nowak",
                "jan.nowak@example.com", null
        );

        candidateScenario.changeStatus(
                wrongStatus.organizationId(),
                wrongStatus.id(),
                CandidateStatus.ARCHIVED
        );

        var query = new CandidateListQuery(
                "jan",
                null,
                Set.of(),
                CandidateStatus.ACTIVE
        );

        var result = repository.search(
                matching.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(matching.id());
    }

    @Test
    void shouldFilterByCompany() {
        var companyA = jobApplicationScenario.create();
        var companyB = jobApplicationScenario.create();

        var query = new CandidateListQuery(
                null,
                companyA.companyId(),
                Set.of(),
                null
        );

        var result = repository.search(
                companyA.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(companyA.candidateId());

        assertThat(result)
                .extracting(CandidateItem::id)
                .doesNotContain(companyB.candidateId());
    }

    @Test
    void shouldCombineSearchAndTagFilters() {
        var matching = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        var wrongTag = candidateScenario.createCandidate(matching.organization(), matching.recruiter(),
                "Jan",
                "Nowak",
                "jan.nowak@example.com", null
        );

        var wrongSearch = candidateScenario.createCandidate(
                matching.organization(),
                matching.recruiter(),
                "Adam",
                "Nowak",
                "adam.nowak@example.com",
                null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        matching.id(),
                        JAVA_TAG
                )
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        wrongTag.id(),
                        DOCKER_TAG
                )
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        wrongSearch.id(),
                        JAVA_TAG
                )
        );

        var query = new CandidateListQuery(
                "kowalski",
                null,
                Set.of(JAVA_TAG),
                null
        );

        var result = repository.search(
                matching.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(matching.id());
    }

    @Test
    void shouldCombineAllFilters() {
        var company =  jobApplicationScenario.create();

        var matching =  company.candidateId();

        candidateTaggingRepository.create(
                new CandidateTagging(
                        matching,
                        JAVA_TAG
                )
        );

        var wrongCompany = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.other@example.com", null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        wrongCompany.id(),
                        JAVA_TAG
                )
        );

        var wrongTag = candidateScenario.createCandidate(company.organization(), company.recruiter(),
                "Jan",
                "Kowalski",
                "jan.tag@example.com", null
        );

        candidateTaggingRepository.create(
                new CandidateTagging(
                        wrongTag.id(),
                        DOCKER_TAG
                )
        );

        var wrongStatus = jobApplicationScenario.createApplication(company);

        candidateTaggingRepository.create(
                new CandidateTagging(
                        wrongStatus.candidateId(),
                        JAVA_TAG
                )
        );

        candidateScenario.changeStatus(
                company.organizationId(),
                wrongStatus.candidateId(),
                CandidateStatus.ARCHIVED
        );

        var query = new CandidateListQuery(
                company.candidateEmail(),
                company.companyId(),
                Set.of(JAVA_TAG),
                CandidateStatus.ACTIVE
        );

        var result = repository.search(
                company.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .containsExactly(matching);
    }

    @Test
    void shouldReturnEmptyPageWhenNoCandidateMatchesFilters() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        var query = new CandidateListQuery(
                "does-not-exist",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result).isEmpty();
        assertThat(result.hasNext()).isFalse();
    }

    @Test
    void shouldIgnoreBlankSearch() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        var query = new CandidateListQuery(
                "   ",
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .contains(candidate.id());
    }

    @Test
    void shouldIgnoreEmptyTagSet() {
        var candidate = candidateScenario.createCandidate(
                "Jan",
                "Kowalski",
                "jan.kowalski@example.com", null
        );

        var query = new CandidateListQuery(
                null,
                null,
                Set.of(),
                null
        );

        var result = repository.search(
                candidate.organizationId(),
                query,
                pageable
        );

        assertThat(result)
                .extracting(CandidateItem::id)
                .contains(candidate.id());
    }
}