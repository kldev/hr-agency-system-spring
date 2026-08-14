package com.pl.hragency.suggestion.adapter.rest;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.company.api.CompanySuggestion;
import com.pl.hragency.identity.api.IdentityApi;
import com.pl.hragency.identity.api.UserSuggestion;
import com.pl.hragency.shared.rest.ExecutionContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/suggestion")
@Tag(name = "Suggestion")
public class SuggestionController {

    private final IdentityApi identityApi;
    private final CompanyApi companyApi;

    public SuggestionController(IdentityApi identityApi, CompanyApi companyApi) {
        this.identityApi = identityApi;
        this.companyApi = companyApi;
    }

    private ExecutionContext getContext() {
        var currentUser = identityApi.getCurrentUser();
        return currentUser.getExecutionContext();
    }

    @GetMapping("/users")
    public List<UserSuggestion> users(@RequestParam(required = false) String search, @RequestParam(required = false) Set<String> roles){
        return identityApi.findUserSuggestions(getContext().organizationId(), search, roles);
    }

    @GetMapping("/companies")
    public List<CompanySuggestion> companies(@RequestParam(required = false) String search, @RequestParam(required = false) String countryCode){
        return companyApi.findCompanySuggestions(getContext().organizationId(), search, countryCode);
    }
}
