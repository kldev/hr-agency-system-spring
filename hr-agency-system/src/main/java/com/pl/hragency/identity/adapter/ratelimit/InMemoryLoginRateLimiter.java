package com.pl.hragency.identity.adapter.ratelimit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import com.pl.hragency.identity.application.port.LoginRateLimiter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class InMemoryLoginRateLimiter implements LoginRateLimiter {

    private final Cache<String, Bucket> ipBuckets =
            Caffeine.newBuilder()
                    .maximumSize(100_000)
                    .expireAfterAccess(Duration.ofHours(1))
                    .build();

    private final Cache<String, Bucket> accountBuckets =
            Caffeine.newBuilder()
                    .maximumSize(100_000)
                    .expireAfterAccess(Duration.ofHours(1))
                    .build();

    @Override
    public boolean tryConsumeIp(String ip) {
        return ipBuckets
                .get(ip, ignored -> createIpBucket())
                .tryConsume(1);
    }

    @Override
    public boolean tryConsumeAccount(
            String organizationSlug,
            String email
    ) {
        var key = accountKey(organizationSlug, email);

        return accountBuckets
                .get(key, ignored -> createAccountBucket())
                .tryConsume(1);
    }

    @Override
    public void resetAccount(
            String organizationSlug,
            String email
    ) {
        accountBuckets.invalidate(
                accountKey(organizationSlug, email)
        );
    }

    private Bucket createIpBucket() {
        var limit = Bandwidth.builder()
                .capacity(10)
                .refillIntervally(10, Duration.ofMinutes(1))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private Bucket createAccountBucket() {
        var limit = Bandwidth.builder()
                .capacity(5)
                .refillIntervally(5, Duration.ofMinutes(15))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    private String accountKey(
            String organizationSlug,
            String email
    ) {
        return organizationSlug.toLowerCase()
                + ":"
                + email.toLowerCase();
    }
}