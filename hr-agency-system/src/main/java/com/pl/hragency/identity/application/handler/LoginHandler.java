package com.pl.hragency.identity.application.handler;

import com.pl.hragency.identity.application.command.LoginCommand;
import com.pl.hragency.identity.application.port.PasswordHasher;
import com.pl.hragency.identity.application.port.TokenGenerator;
import com.pl.hragency.identity.application.port.UserRepository;
import com.pl.hragency.identity.application.result.LoginResult;
import com.pl.hragency.identity.domain.exception.InvalidLoginCommandException;
import com.pl.hragency.identity.domain.exception.InvalidPasswordException;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.identity.domain.model.UserOrganizationId;
import com.pl.hragency.organization.api.OrganizationApi;
import org.springframework.stereotype.Service;

@Service
public class LoginHandler {
    private final UserRepository repository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;
    private final OrganizationApi  organizationApi;

    public LoginHandler(UserRepository repository,
                        PasswordHasher passwordHasher,
                        TokenGenerator tokenGenerator,
                        OrganizationApi organizationApi
    ) {
        this.repository = repository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
        this.organizationApi = organizationApi;
    }

    public LoginResult handle(LoginCommand command) {

        var organization = organizationApi.findBySlug(command.orgSlug());

        var organizationId =
                new UserOrganizationId(organization.id());

        var user =
                repository.findByEmailAndOrganizationId(
                                command.email(), organizationId)
                        .orElseThrow(() -> new InvalidLoginCommandException("Invalid email or password"));

        if (user.role() == OrganizationRole.SYSTEM)
            throw new InvalidLoginCommandException("Not allowed to use system organization");

        if(!passwordHasher.matches(
                command.password(),
                user.passwordHash()
        )){
            throw new InvalidLoginCommandException("Invalid email or password");
        }

        var token = tokenGenerator.generate(user);

        return new LoginResult(token);
    }
}
