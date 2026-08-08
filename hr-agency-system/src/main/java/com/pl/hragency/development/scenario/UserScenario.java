package com.pl.hragency.development.scenario;

import com.pl.hragency.identity.api.IdentityApi;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UserScenario {
    private final IdentityApi api;

    public UserScenario(IdentityApi api) {
        this.api = api;
    }

    public List<UUID> create(UUID organizationId) {

        List<UUID> users = new ArrayList<>();
        String testPassword = "pass123";
        api.createUser("admin@hr-agency.mail", "Admin", "Agency", "ADMIN", organizationId, testPassword);
        users.add(api.createUser("a.kowalska@hr-agency.mail", "Anna", "Kowalska", "RECRUITER", organizationId, testPassword));
        users.add(api.createUser("t.brand@hr-agency.mail", "Tom", "Brand", "SALES", organizationId, testPassword));
        users.add(api.createUser("m.cross@hr-agency.mail", "Michael", "Cross", "SALES", organizationId, testPassword));
        users.add(api.createUser("c.boss@hr-agency.mail", "Clif", "Bossman", "SALES", organizationId, testPassword));

        return users;
    }



}
