package com.pl.hragency.development.scenario;

import com.pl.hragency.identity.api.IdentityApi;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
public class UserScenario {

    private static final String TEST_PASSWORD = "pass123";

    private final IdentityApi api;
    private final Faker faker;


    public UserScenario(IdentityApi api) {
        this.api = api;
        this.faker = new Faker(Locale.forLanguageTag("pl"));
    }

    public void createPlatformOwner(){
        api.createPlatformUser(
                "root",
                "OWNER",
                TEST_PASSWORD
        );
    }

    public List<UUID> create(UUID organizationId, String slug) {

        List<UUID> users = new ArrayList<>();

        api.createUser(
                "admin@%s.mail".formatted(slug),
                "Admin",
                "Agency",
                "ADMIN",
                organizationId,
                TEST_PASSWORD
        );

        users.add(api.createUser(
                "a.kowalska@%s.mail".formatted(slug),
                "Anna",
                "Kowalska",
                "RECRUITER",
                organizationId,
                TEST_PASSWORD
        ));

        users.add(api.createUser(
                "t.brand@%s.mail".formatted(slug),
                "Tom",
                "Brand",
                "SALES",
                organizationId,
                TEST_PASSWORD
        ));

        users.add(api.createUser(
                "m.cross@%s.mail".formatted(slug),
                "Michael",
                "Cross",
                "SALES",
                organizationId,
                TEST_PASSWORD
        ));

        users.add(api.createUser(
                "c.boss@%s.mail".formatted(slug),
                "Clif",
                "Bossman",
                "SALES",
                organizationId,
                TEST_PASSWORD
        ));

        createUsers(
                users,
                organizationId,
                slug,
                "RECRUITER",
                10
        );

        createUsers(
                users,
                organizationId,
                slug,
                "SALES",
                10
        );

        createUsers(
                users,
                organizationId,
                slug,
                "INTERVIEWER",
                5
        );

        return users;
    }

    private void createUsers(
            List<UUID> users,
            UUID organizationId,
            String slug,
            String role,
            int count
    ) {
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();

            users.add(api.createUser(
                    buildEmail(firstName, lastName, slug, i),
                    firstName,
                    lastName,
                    role,
                    organizationId,
                    TEST_PASSWORD
            ));
        }
    }

    private String buildEmail(
            String firstName,
            String lastName,
            String slug,
            int index
    ) {
        return "%s.%s.%d@%s.mail".formatted(
                normalize(firstName),
                normalize(lastName),
                index,
                slug
        );
    }

    private String normalize(String value) {
        return value
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "");
    }
}