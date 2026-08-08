package com.pl.hragency.jobdescription.domain.model;

import java.math.BigDecimal;
import java.util.Currency;

public record SalaryRange(
        BigDecimal min,
        BigDecimal max,
        Currency currency
) {

    public SalaryRange {
        if (min == null) {
            throw new IllegalArgumentException("Minimum salary is required");
        }

        if (max == null) {
            throw new IllegalArgumentException("Maximum salary is required");
        }

        if (min.signum() < 0 || max.signum() < 0) {
            throw new IllegalArgumentException(
                    "Salary cannot be negative"
            );
        }

        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException(
                    "Minimum salary cannot exceed maximum salary"
            );
        }

        if (currency == null) {
            throw new IllegalArgumentException(
                    "Salary currency is required"
            );
        }
    }
}
