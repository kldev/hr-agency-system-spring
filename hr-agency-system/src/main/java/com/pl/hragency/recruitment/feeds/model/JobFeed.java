package com.pl.hragency.recruitment.feeds.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "jobs")
public record JobFeed (
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "job")
    List<JobFeedItem> jobs
){}
