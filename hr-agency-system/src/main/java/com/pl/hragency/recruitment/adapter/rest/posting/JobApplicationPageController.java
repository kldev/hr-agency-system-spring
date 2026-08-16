package com.pl.hragency.recruitment.adapter.rest.posting;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.organization.api.OrganizationApi;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationHandler;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.application.query.JobPostingItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateSource;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingId;
import com.pl.hragency.shared.event.UserSnapshot;
import com.pl.hragency.shared.rest.ExecutionContext;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("public/")
public class JobApplicationPageController {

    private final Logger logger = LoggerFactory.getLogger(JobApplicationPageController.class);
    private final JobPostingRepository service;
    private final OrganizationApi api;
    private final CreateJobApplicationHandler handler;
    private final IdentityApi identityApi;

    public JobApplicationPageController(JobPostingRepository service,
                                        OrganizationApi api,
                                        CreateJobApplicationHandler handler, IdentityApi identityApi) {
        this.service = service;
        this.api = api;
        this.handler = handler;
        this.identityApi = identityApi;
    }

    @GetMapping("/{slug}/apply/{id}")
    public String apply(
            @PathVariable UUID id,
            Model model,
            @PathVariable String slug) {
        var organization = api.findBySlug(slug);

        var jobPosting = service.findById(organization.id(),
                new JobPostingId((id))).map(JobPostingItem::from).orElse(null);

        if (jobPosting == null) {
            return "redirect:/";
        }

        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute(
                "command",
                new CreateJobApplicationCommand(jobPosting.id(),
                        "", "", "", "", CandidateSource.CAREER_PAGE )
        );
        model.addAttribute("slug", slug);

        return "apply";
    }

    @PostMapping("/{slug}/apply/{id}")
    public String apply(
            @PathVariable String slug,
            @PathVariable UUID id,
            @Valid @ModelAttribute("command") CreateJobApplicationCommand command,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Applying job application: {} {}", slug, command.email());
        var organization = api.findBySlug(slug);

        if (organization == null) {
            return "redirect:/";
        }

        var jobPosting = service.findById(
                organization.id(),
                new JobPostingId(id)
        ).map(JobPostingItem::from).orElse(null);

        if (jobPosting == null) {
            return "redirect:/";
        }

        if (command.email().isBlank()) {
            model.addAttribute("jobPosting", jobPosting);
            model.addAttribute("command", command);

            return "apply";
        }

        var saveCommand = new CreateJobApplicationCommand(jobPosting.id(),
                command.email(),
                command.firstName(),
                command.lastName(),
                command.phone(),
                CandidateSource.CAREER_PAGE );

        var user = identityApi.findByEmail("system", organization.id())
                .orElse(new UserSnapshot(UUID.randomUUID(), "", ""));
        handler.handle(
                new ExecutionContext(organization.id(), user.id(), user.fullName()),
                saveCommand
        );

        return "redirect:/public/{slug}/apply/{id}/success";
    }

    @GetMapping("/{slug}/apply/{id}/success")
    public String success(@PathVariable String slug,
                          @PathVariable UUID id,
                          Model model){

        return "success";
    }

    @GetMapping("/{slug}")
    public String jobs(
            @PathVariable String slug,
            Model model
    ) {
        var organization = api.findBySlug(slug);

        if (organization == null) {
            return "redirect:/";
        }

        model.addAttribute("slug", slug);
        model.addAttribute("organizationName", organization.name());

        return "jobs";
    }

}
