package com.pl.hragency.identity.adapter.security;

import com.pl.hragency.identity.application.port.TokenGenerator;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.identity.domain.model.PlatformOwner;
import com.pl.hragency.identity.domain.model.PlatformRole;
import com.pl.hragency.identity.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService implements TokenGenerator {

    private static final Duration EXPIRATION = Duration.ofHours(1);

    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_ORGANIZATION_ID = "orgId";
    private static final String CLAIM_ROLES = "roles";
    private static final String CLAIM_TYPE = "type";
    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_FULL_NAME = "fullName";

    private final JwtConfig config;

    public JwtService(JwtConfig config) {
        this.config = config;
    }

    @Override
    public String generate(User user) {

        return Jwts.builder()
                .subject(user.id().value().toString())
                .issuedAt(new Date())
                .expiration(expirationDate())
                .claim(CLAIM_USER_ID, user.id().value().toString())
                .claim(
                        CLAIM_ORGANIZATION_ID,
                        user.organizationId().value().toString()
                )
                .claim(CLAIM_EMAIL, user.email())
                .claim(
                        CLAIM_ROLES,
                        List.of(user.role().name())
                )
                .claim(
                        CLAIM_TYPE,
                        JwtSubjectType.ORGANIZATION.name()
                )
                .claim(
                        CLAIM_FULL_NAME,
                        user.firstName() + " " + user.lastName()
                )
                .signWith(getKey())
                .compact();
    }

    @Override
    public String generate(PlatformOwner owner) {

        return Jwts.builder()
                .subject(owner.id().value().toString())
                .issuedAt(new Date())
                .expiration(expirationDate())
                .claim(CLAIM_USER_ID, owner.id().value().toString())
                .claim(CLAIM_EMAIL, owner.email())
                .claim(
                        CLAIM_ROLES,
                        List.of(owner.role().name())
                )
                .claim(
                        CLAIM_TYPE,
                        JwtSubjectType.PLATFORM.name()
                )
                .signWith(getKey())
                .compact();
    }

    public JwtSubjectType extractSubjectType(String token) {

        String type = parseToken(token)
                .get(CLAIM_TYPE, String.class);

        if (type == null) {
            throw new IllegalArgumentException("JWT subject type is missing");
        }

        return JwtSubjectType.valueOf(type);
    }

    public UUID extractUserId(String token) {

        String userId = parseToken(token)
                .get(CLAIM_USER_ID, String.class);

        if (userId == null) {
            throw new IllegalArgumentException("JWT userId is missing");
        }

        return UUID.fromString(userId);
    }

    public UUID extractOrganizationId(String token) {

        String organizationId = parseToken(token)
                .get(CLAIM_ORGANIZATION_ID, String.class);

        if (organizationId == null) {
            return null;
        }

        return UUID.fromString(organizationId);
    }

    public String extractEmail(String token) {
        return parseToken(token)
                .get(CLAIM_EMAIL, String.class);
    }

    public String extractFullName(String token) {
        return parseToken(token)
                .get(CLAIM_FULL_NAME, String.class);
    }

    public List<String> extractRoles(String token) {

        Object value = parseToken(token).get(CLAIM_ROLES);

        if (!(value instanceof List<?> list)) {
            throw new IllegalArgumentException("JWT roles claim is invalid");
        }

        return list.stream()
                .map(r -> {
                    if (!(r instanceof String role)) {
                        throw new IllegalArgumentException(
                                "JWT role must be a string"
                        );
                    }

                    return role;
                })
                .toList();
    }

    private Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Date expirationDate() {
        return new Date(
                System.currentTimeMillis()
                        + EXPIRATION.toMillis()
        );
    }

    private SecretKey getKey() {

        return Keys.hmacShaKeyFor(
                config.getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    public SecurityUser extractSecurityUser(String token) {

        JwtSubjectType type = extractSubjectType(token);

        if (type != JwtSubjectType.ORGANIZATION) {
            throw new IllegalArgumentException(
                    "JWT does not represent an organization user"
            );
        }

        UUID userId = extractUserId(token);
        UUID organizationId = extractOrganizationId(token);

        if (organizationId == null) {
            throw new IllegalArgumentException(
                    "Organization ID is missing"
            );
        }

        return new SecurityUser(
                userId,
                extractEmail(token),
                organizationId,
                extractRoles(token).stream()
                        .map(OrganizationRole::valueOf)
                        .toList(),
                extractFullName(token)
        );
    }

    public PlatformOwnerSecurityUser extractPlatformOwner(String token) {

        JwtSubjectType type = extractSubjectType(token);

        if (type != JwtSubjectType.PLATFORM) {
            throw new IllegalArgumentException(
                    "JWT does not represent a platform owner"
            );
        }

        return new PlatformOwnerSecurityUser(
                extractUserId(token),
                extractEmail(token),
                extractRoles(token).stream()
                        .map(PlatformRole::valueOf)
                        .toList().getFirst()
        );
    }

}

