package com.pl.hragency.recruitment.integration.adapter.rest;

import com.pl.hragency.recruitment.integration.application.command.SubmitJobApplicationCommand;
import com.pl.hragency.shared.rest.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/integrations/job-applications")
@Tag(name = "Recruitment")
public class RecruitmentIntegrationController {

    private final Logger logger = LoggerFactory.getLogger(RecruitmentIntegrationController.class);

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_APPLICATION_CREATE')")
    public ApiResult submitJobApplication(@Validated @RequestBody SubmitJobApplicationCommand command)
    {
        logger.info("submitting job application {} {}", command.jobPostingId(), command.email());

        return new ApiResult("Application submitted", true);
    }
}
