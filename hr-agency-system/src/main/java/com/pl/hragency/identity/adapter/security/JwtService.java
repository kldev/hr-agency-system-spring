package com.pl.hragency.identity.adapter.security;


import com.pl.hragency.identity.application.port.TokenGenerator;
import com.pl.hragency.identity.domain.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService implements TokenGenerator {

    private static final long EXPIRATION = 3_600_000;

    private final JwtConfig config;

    public JwtService(JwtConfig config) {
        this.config = config;
    }

    @Override
    public String generate(User user) {

        List<String> roles = List.of(
                user.role().name()
        );

        return Jwts.builder()
                .subject(user.email())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis() + EXPIRATION
                        )
                )
                .claim("userId", user.id().value().toString())
                .claim("orgId", user.organizationId().value().toString())
                .claim("roles", roles)
                .signWith(getKey())
                .compact();
    }

    public String extractUsername(String token) {

        return parseToken(token)
                .getSubject();
    }

    public UUID extractUserId(String token) {

        String userId = parseToken(token)
                .get("userId", String.class);

        return UUID.fromString(userId);
    }

    public UUID extractOrganizationId(String token) {

        String organizationId = parseToken(token)
                .get("orgId", String.class);

        return UUID.fromString(organizationId);
    }

    private io.jsonwebtoken.Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                config.getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }
}
