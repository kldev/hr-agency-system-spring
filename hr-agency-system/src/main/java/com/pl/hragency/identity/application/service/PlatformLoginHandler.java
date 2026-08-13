package com.pl.hragency.identity.application.service;


import com.pl.hragency.identity.application.command.OwnerLoginCommand;
import com.pl.hragency.identity.application.port.PasswordHasher;
import com.pl.hragency.identity.application.port.PlatformUserRepository;
import com.pl.hragency.identity.application.port.TokenGenerator;
import com.pl.hragency.identity.application.result.LoginResult;
import com.pl.hragency.identity.domain.exception.InvalidLoginCommandException;
import com.pl.hragency.identity.domain.exception.InvalidPasswordException;
import com.pl.hragency.identity.domain.model.PlatformOwner;
import org.springframework.stereotype.Service;

@Service
public class PlatformLoginHandler {
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;
    private final PlatformUserRepository repository;

    public PlatformLoginHandler(PasswordHasher passwordHasher, TokenGenerator tokenGenerator, PlatformUserRepository repository) {
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
        this.repository = repository;
    }

    public LoginResult handle(OwnerLoginCommand command){
        PlatformOwner user =
                repository.findByEmail(
                                command.email())
                        .orElseThrow(() -> new InvalidLoginCommandException("Invalid email or password"));

        if(!passwordHasher.matches(
                command.password(),
                user.passwordHash()
        )){
            throw new InvalidPasswordException(
                    "Invalid password");
        }

        String token = tokenGenerator.generate(user);

        return new LoginResult(token);
    }
}

