package com.pl.hragency.identity.application.port;

public interface LoginRateLimiter {

    boolean tryConsumeIp(String ip);

    boolean tryConsumeAccount(String organizationSlug, String email);

    void resetAccount(String organizationSlug, String email);
}
