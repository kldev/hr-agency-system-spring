package com.pl.hragency.identity.adapter.ratelimit;


import com.pl.hragency.identity.application.port.LoginRateLimiter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "app.rate-limit",
        name = "enabled",
        havingValue = "false",
        matchIfMissing = true
)
public class NoOpLoginRateLimiter implements LoginRateLimiter {
    @Override
    public boolean tryConsumeIp(String ip) {
        return true;
    }

    @Override
    public boolean tryConsumeAccount(String organizationSlug, String email) {
        return true;
    }

    @Override
    public void resetAccount(String organizationSlug, String email) {

    }
}
