package com.pl.hragency.company.application.query;

import java.util.List;

public record CompanyDetailsItem(CompanyListItem company, List<CompanyContactItem> contacts) {
}
