package com.pl.hragency.company.adapter.rest;

import com.pl.hragency.company.application.command.CreateCompanyContactCommand;
import com.pl.hragency.company.application.handler.CreateCompanyContactHandler;
import com.pl.hragency.company.domain.model.CompanyContactCompanyId;
import com.pl.hragency.company.domain.model.CompanyContactId;
import com.pl.hragency.identity.api.CurrentUser;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/company")
@Tag(name = "Company")
public class CompanyContactController {

    private final CreateCompanyContactHandler createCompanyContactHandler;
    private final IdentityApi identityApi;

    public CompanyContactController(CreateCompanyContactHandler createCompanyContactHandler, IdentityApi identityApi) {
        this.createCompanyContactHandler = createCompanyContactHandler;
        this.identityApi = identityApi;
    }

    private ExecutionContext getContext() {
        CurrentUser currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @PostMapping("{companyId}/contact")
    public ResponseEntity<CompanyContactId> createContact(@RequestBody CreateCompanyContactCommand command, @PathVariable UUID companyId){
        var result = createCompanyContactHandler.handle(getContext(), new CompanyContactCompanyId(companyId), command);
        return ResponseEntity.ok(result);
    }
}



