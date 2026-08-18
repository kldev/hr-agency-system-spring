package com.pl.hragency.recruitment.feeds;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;

import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.feeds.application.GeneratedJobFeed;
import com.pl.hragency.recruitment.feeds.application.JobFeedGenerator;
import com.pl.hragency.recruitment.feeds.model.JobFeed;
import com.pl.hragency.recruitment.feeds.model.JobFeedItem;
import com.pl.hragency.recruitment.feeds.model.JobFeedItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobFeedGeneratorTest {

    @Mock
    private JobPostingRepository repository;

    @Mock
    private JobFeedItemMapper mapper;

    private XmlMapper xmlMapper;
    private JsonMapper jsonMapper;

    private JobFeedGenerator generator;

    @BeforeEach
    void setUp() {
        xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        jsonMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        generator = new JobFeedGenerator(
                repository,
                xmlMapper,
                jsonMapper,
                mapper
        );
    }

    @Test
    void shouldGenerateXmlAndJsonFeed() throws Exception {
        UUID organizationId = UUID.randomUUID();

        UUID jobId1 = UUID.randomUUID();
        UUID jobId2 = UUID.randomUUID();

        var posting1 = createJobPosting("Java Developer");
        var posting2 = createJobPosting("Rust/Go Developer");

        var item1 = createJobFeedItem(
                jobId1,
                "Java Developer"
        );

        var item2 = createJobFeedItem(
                jobId2,
                "Senior Java Developer"
        );

        when(repository.search(
                eq(organizationId),
                eq(JobPostingListQuery.published()),
                eq(PageRequest.of(0, 9999))
        )).thenReturn(new PageImpl<>(
                List.of(posting1, posting2)
        ));

        when(mapper.toItem(posting1)).thenReturn(item1);
        when(mapper.toItem(posting2)).thenReturn(item2);

        GeneratedJobFeed result = generator.generate(organizationId);

        assertThat(result.xml()).isNotEmpty();
        assertThat(result.json()).isNotEmpty();

        var jsonFeed = jsonMapper.readValue(
                result.json(),
                JobFeed.class
        );

        var xmlFeed = xmlMapper.readTree(result.xml());

        assertThat(xmlFeed)
                .isNotNull();

        var jobs = xmlFeed.get("job");

        assertXmlJob(jobs.get(0), item1);
        assertXmlJob(jobs.get(1), item2);

        verify(repository).search(
                organizationId,
                JobPostingListQuery.published(),
                PageRequest.of(0, 9999)
        );

        verify(mapper).toItem(posting1);
        verify(mapper).toItem(posting2);
    }

    private void assertXmlJob(
            JsonNode node,
            JobFeedItem expected
    ) {
        assertThat(node.get("id").asText())
                .isEqualTo(expected.id().toString());

        assertThat(node.get("title").asText())
                .isEqualTo(expected.title());

        assertThat(node.get("summary").asText())
                .isEqualTo(expected.summary());

        assertThat(node.get("description").asText())
                .isEqualTo(expected.description());

        assertThat(node.get("location").asText())
                .isEqualTo(expected.location());

        assertThat(node.get("countryCode").asText())
                .isEqualTo(expected.countryCode());
    }

    @Test
    void shouldGenerateEmptyFeedWhenNoJobPostingsExist()
            throws Exception {

        UUID organizationId = UUID.randomUUID();

        when(repository.search(
                eq(organizationId),
                eq(JobPostingListQuery.published()),
                eq(PageRequest.of(0, 9999))
        )).thenReturn(new PageImpl<>(List.of()));

        GeneratedJobFeed result = generator.generate(organizationId);

        assertThat(result.xml()).isNotEmpty();
        assertThat(result.json()).isNotEmpty();

        var jsonFeed = jsonMapper.readValue(
                result.json(),
                JobFeed.class
        );

        var xmlFeed = xmlMapper.readTree(
                result.xml()
        );

        assertThat(jsonFeed.jobs())
                .isEmpty();

        var xmlJobs = xmlFeed.get("job");

        assertThat(xmlJobs == null || xmlJobs.isEmpty())
                .isTrue();

        verifyNoInteractions(mapper);
    }

    @Test
    void shouldUsePublishedQuery() throws Exception {
        UUID organizationId = UUID.randomUUID();

        when(repository.search(
                any(),
                any(),
                any()
        )).thenReturn(new PageImpl<>(List.of()));

        generator.generate(organizationId);

        ArgumentCaptor<JobPostingListQuery> queryCaptor =
                ArgumentCaptor.forClass(JobPostingListQuery.class);

        verify(repository).search(
                eq(organizationId),
                queryCaptor.capture(),
                eq(PageRequest.of(0, 9999))
        );

        assertThat(queryCaptor.getValue())
                .isEqualTo(JobPostingListQuery.published());
    }

    @Test
    void shouldMapEveryJobPostingToFeedItem() throws Exception {
        UUID organizationId = UUID.randomUUID();

        var posting1 = createJobPosting("Java Developer");
        var posting2 = createJobPosting("Kotlin Developer");
        var posting3 = createJobPosting("Backend Developer");

        var item1 = createJobFeedItem(
                UUID.randomUUID(),
                "Java Developer"
        );

        var item2 = createJobFeedItem(
                UUID.randomUUID(),
                "Kotlin Developer"
        );

        var item3 = createJobFeedItem(
                UUID.randomUUID(),
                "Backend Developer"
        );

        when(repository.search(
                eq(organizationId),
                eq(JobPostingListQuery.published()),
                eq(PageRequest.of(0, 9999))
        )).thenReturn(new PageImpl<>(
                List.of(posting1, posting2, posting3)
        ));

        when(mapper.toItem(posting1)).thenReturn(item1);
        when(mapper.toItem(posting2)).thenReturn(item2);
        when(mapper.toItem(posting3)).thenReturn(item3);

        generator.generate(organizationId);

        verify(mapper).toItem(posting1);
        verify(mapper).toItem(posting2);
        verify(mapper).toItem(posting3);
        verifyNoMoreInteractions(mapper);
    }

    @Test
    void shouldUseOrganizationIdWhenSearchingForJobPostings()
            throws Exception {

        UUID organizationId = UUID.randomUUID();

        when(repository.search(
                any(),
                any(),
                any()
        )).thenReturn(new PageImpl<>(List.of()));

        generator.generate(organizationId);

        verify(repository).search(
                eq(organizationId),
                eq(JobPostingListQuery.published()),
                eq(PageRequest.of(0, 9999))
        );
    }

    private JobFeedItem createJobFeedItem(
            UUID id,
            String title
    ) {
        Instant now = Instant.now();

        return new JobFeedItem(
                id,
                title,
                "Job summary",
                "Job description",
                "http://localhost:8080/acme/apply/job-title-a13a",
                List.of(
                        "Develop backend applications",
                        "Write clean code"
                ),
                List.of(
                        "3+ years of Java experience",
                        "Knowledge of Spring Boot"
                ),
                List.of(
                        "Java",
                        "Spring Boot",
                        "PostgreSQL"
                ),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new SalaryRange(
                        BigDecimal.valueOf(12000),
                        BigDecimal.valueOf(18000),
                        Currency.getInstance("PLN")
                ),
                now
        );
    }

    private JobPosting createJobPosting(String title) {
        return JobPosting.draft(UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                title,
                "Job summary",
                "job description",
                List.of("responsibility 1", "responsibility 2", "responsibility 3"),
                List.of("requirement 1", "requirement 2", "requirement 3"),
                List.of("skill 1", "skill 2", "skill 3"),
                "London" , "UK",
                EmploymentType.FULL_TIME,
                WorkMode.REMOTE,
                new SalaryRange(BigDecimal.valueOf(3000),
                        BigDecimal.valueOf(5000),
                        Currency.getInstance("EUR")),
                "job-summary-as33",
                "acme"
                );
    }
}