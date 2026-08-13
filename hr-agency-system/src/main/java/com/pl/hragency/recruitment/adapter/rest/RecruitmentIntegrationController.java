package com.pl.hragency.recruitment.adapter.rest;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationHandler;

import com.pl.hragency.recruitment.domain.result.ApplyForPostingResult;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.shared.rest.ExecutionContext;
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
@Tag(name = "Integrations")
public class RecruitmentIntegrationController {

    private final Logger logger = LoggerFactory.getLogger(RecruitmentIntegrationController.class);
    private final IdentityApi identityApi;
    private final CreateJobApplicationHandler handler;

    public RecruitmentIntegrationController(IdentityApi identityApi,
                                            CreateJobApplicationHandler handler) {
        this.identityApi = identityApi;
        this.handler = handler;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_APPLICATION_WRITE')")
    public ApplyForPostingResult submitJobApplication(@Validated @RequestBody CreateJobApplicationCommand command) {
        var client = identityApi.gCurrentIntegrationClient();

        logger.info("submitting job application {} {} - client {}",
                command.jobPostingId(), command.email(),
                client.clientName()
        );

        return handler.handle(client.getExecutionContext(), command);

    }
}
