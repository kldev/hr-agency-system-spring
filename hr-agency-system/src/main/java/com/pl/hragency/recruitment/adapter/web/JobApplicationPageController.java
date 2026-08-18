package com.pl.hragency.recruitment.adapter.web;

import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.organization.api.OrganizationApi;
import com.pl.hragency.recruitment.application.command.CreateJobApplicationCommand;
import com.pl.hragency.recruitment.application.handler.CreateJobApplicationHandler;
import com.pl.hragency.recruitment.application.port.JobPostingRepository;
import com.pl.hragency.recruitment.application.query.JobPostingItem;
import com.pl.hragency.recruitment.application.query.JobPostingItemMapper;
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
    private final JobPostingItemMapper mapper;

    public JobApplicationPageController(JobPostingRepository service,
                                        OrganizationApi api,
                                        CreateJobApplicationHandler handler,
                                        IdentityApi identityApi,
                                        JobPostingItemMapper mapper) {
        this.service = service;
        this.api = api;
        this.handler = handler;
        this.identityApi = identityApi;
        this.mapper = mapper;
    }

    @GetMapping("/{slug}/apply/{postingSlug}")
    public String apply(
            Model model,
            @PathVariable String slug, @PathVariable String postingSlug) {
        var organization = api.findBySlug(slug);

        var jobPosting = service.findBySlug(organization.id(),
                postingSlug).map(mapper::from).orElse(null);

        if (jobPosting == null) {
            return "redirect:/";
        }

        model.addAttribute("jobPosting", jobPosting);
        model.addAttribute(
                "command",
                new JobApplicationForm(jobPosting.id(),
                        "", "", "", "")
        );
        model.addAttribute("slug", slug);
        model.addAttribute("postingSlug", postingSlug);

        return "apply";
    }

    @PostMapping("/{slug}/apply/{postingSlug}")
    public String apply(
            @PathVariable String slug,
            @PathVariable String postingSlug,
            @Valid @ModelAttribute("command") JobApplicationForm form,
            BindingResult bindingResult,
            Model model
    ) {
        logger.info("Applying job application: {} {}", slug, form.email());
        var organization = api.findBySlug(slug);

        if (organization == null) {
            return "redirect:/";
        }

        var jobPosting = service.findBySlug(
                organization.id(),
                postingSlug
        ).map(mapper::from).orElse(null);

        if (jobPosting == null) {
            return "redirect:/";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("jobPosting", jobPosting);
            model.addAttribute("command", form);
            model.addAttribute("slug", slug);
            model.addAttribute("postingSlug", postingSlug);

            return "apply";
        }

        var command = form.toCommand(jobPosting.id());

        var user = identityApi.findByEmail("system", organization.id())
                .orElse(new UserSnapshot(UUID.randomUUID(), "", ""));
        handler.handle(
                new ExecutionContext(organization.id(), user.id(), user.fullName()),
                command
        );

        return "redirect:/public/{slug}/apply/{postingSlug}/success";
    }

    @GetMapping("/{slug}/apply/{postingSlug}/success")
    public String success(@PathVariable String slug,
                          @PathVariable String postingSlug,
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
