package com.pl.hragency.company.adapter.persistence;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CompanyQueryRepository {
    List<CompanySuggestionProjection> findSuggestions(Specification<CompanyJpaEntity> specification, Pageable pageable);
}
