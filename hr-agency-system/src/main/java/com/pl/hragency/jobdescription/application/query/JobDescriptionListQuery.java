package com.pl.hragency.jobdescription.application.query;

import org.springframework.data.domain.Pageable;

public record JobDescriptionListQuery(String search, Pageable pageable) {
}
