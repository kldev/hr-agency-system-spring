package com.pl.hragency.jobdescription.application.command;

import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.WorkMode;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateJobDescriptionCommand(

        @NotNull(message = "Company is required")
        UUID companyId,

        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @NotBlank(message = "Summary is required")
        @Size(max = 1000, message = "Summary must not exceed 1000 characters")
        String summary,

        @NotBlank(message = "Description is required")
        String description,

        @NotEmpty(message = "At least one responsibility is required")
        List<@NotBlank(message = "Responsibility must not be blank") String> responsibilities,

        @NotEmpty(message = "At least one requirement is required")
        List<@NotBlank(message = "Requirement must not be blank") String> requirements,

        List<@NotBlank(message = "Skill must not be blank") String> skills,

        String location,

        @NotBlank(message = "Country code is required")
        @Pattern(
                regexp = "^[A-Z]{2}$",
                message = "Country code must be a 2-letter ISO code"
        )
        String countryCode,

        @NotNull(message = "Employment type is required")
        EmploymentType employmentType,

        @NotNull(message = "Work mode is required")
        WorkMode workMode,

        @PositiveOrZero(message = "Minimum salary must be greater than or equal to 0")
        BigDecimal salaryMin,

        @PositiveOrZero(message = "Maximum salary must be greater than or equal to 0")
        BigDecimal salaryMax,

        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Salary currency must be a 3-letter ISO code"
        )
        String salaryCurrency

) {
}