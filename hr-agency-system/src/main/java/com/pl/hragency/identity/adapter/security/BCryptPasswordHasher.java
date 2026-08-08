package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
    private final PasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    public String hash(String password){
        return encoder.encode(password);
    }
    public boolean matches(
            String password,
            String hash){

        return encoder.matches(
                password,
                hash);

    }

}

