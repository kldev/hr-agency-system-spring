package com.pl.hragency.company.application.query;

import org.springframework.data.domain.Pageable;

public record CompanyListQuery(
        String search,
        Pageable pageable
) {
}
