package com.pl.hragency.recruitment.feeds.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.application.query.JobPostingListQuery;
import com.pl.hragency.recruitment.feeds.model.JobFeed;
import com.pl.hragency.recruitment.feeds.model.JobFeedItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JobFeedGenerator {

    private final Logger logger = LoggerFactory.getLogger(JobFeedGenerator.class);

    private final JobPostingRepository repository;
    private final XmlMapper xmlMapper;
    private final JsonMapper jsonMapper;
    private final JobFeedItemMapper mapper;

    public JobFeedGenerator(
            JobPostingRepository repository, XmlMapper xmlMapper, JsonMapper jsonMapper,
            JobFeedItemMapper mapper
    ) {
        this.repository = repository;
        this.xmlMapper = xmlMapper;
        this.jsonMapper = jsonMapper;
        this.mapper = mapper;
    }

    public GeneratedJobFeed generate(UUID organizationId)
            throws JsonProcessingException {

        var query = JobPostingListQuery.published();

        var jobs = repository.search(
                        organizationId,
                        query,
                        PageRequest.of(0, 9999)
                )
                .stream()
                .map(mapper::toItem)
                .toList();

        var feed = new JobFeed(jobs);

        byte[] xml = xmlMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(feed);

        byte[] json = jsonMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsBytes(feed);

        logger.info("Generate JobFeed completed {}", organizationId);
        return new GeneratedJobFeed(
               xml, json);
    }
}