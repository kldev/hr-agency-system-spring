package com.pl.hragency.company.application.query;

import com.pl.hragency.company.adapter.persistence.*;
import com.pl.hragency.company.api.CompanySuggestion;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CompanySuggestionsQueryService implements CompanySuggestionsQuery{
    private final CompanyQueryRepository companyRepository;

    public CompanySuggestionsQueryService(CompanyQueryRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<CompanySuggestion> find(UUID organizationId, String search, String countryCode) {

        Specification<CompanyJpaEntity> specification = Specification.allOf(
                CompanySpecifications.organizationId(organizationId),
                CompanySpecifications.search(search),
                CompanySpecifications.countryCode(countryCode)
        );
        Pageable pageable = PageRequest.of(0, 25,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        return companyRepository.findSuggestions(specification, pageable).stream().map(this::toCompanySuggestion).toList();
    }

    private CompanySuggestion toCompanySuggestion(CompanySuggestionProjection input){
        return new CompanySuggestion(input.id(), input.name(), input.taxId(), input.countryCode());
    }
}
