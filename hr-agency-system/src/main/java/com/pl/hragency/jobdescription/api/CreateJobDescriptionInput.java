package com.pl.hragency.jobdescription.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateJobDescriptionInput(UUID companyId,
                                        String title,
                                        String summary,
                                        String description,
                                        List<String> skills,
                                        List<String> responsibilities,
                                        List<String> requirements,
                                        String location,
                                        String workMode,
                                        BigDecimal salaryMin,
                                        BigDecimal salaryMax) {
}
