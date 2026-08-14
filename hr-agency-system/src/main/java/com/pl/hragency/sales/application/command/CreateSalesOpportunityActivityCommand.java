package com.pl.hragency.sales.application.command;

import com.pl.hragency.sales.domain.model.SalesActivityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateSalesOpportunityActivityCommand(
        @NotBlank(message = "Note is required")
        @Size(max = 500, message = "Title must not exceed 500 characters")
        String note, SalesActivityType type) {
}
