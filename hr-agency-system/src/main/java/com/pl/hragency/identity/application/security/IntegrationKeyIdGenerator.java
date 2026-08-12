package com.pl.hragency.identity.application.security;

import java.security.SecureRandom;

public class IntegrationKeyIdGenerator {
    private static final String CHARACTERS =
            "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    private IntegrationKeyIdGenerator() {
    }

    public static String generate(int length) {
        StringBuilder password = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }

        return password.toString();
    }

    public static String generateDefault() {
        return generate(8);
    }
}
