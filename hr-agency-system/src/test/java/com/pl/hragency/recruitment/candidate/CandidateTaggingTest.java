package com.pl.hragency.recruitment.candidate;

import com.pl.hragency.BaseRestIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.recruitment.application.command.TagCandidateCommand;
import com.pl.hragency.recruitment.application.port.CandidateTaggingRepository;
import com.pl.hragency.recruitment.application.query.CandidateTagItem;
import com.pl.hragency.recruitment.domain.model.candidate.CandidateId;
import com.pl.hragency.shared.rest.ApiResult;
import com.pl.hragency.testsupport.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class CandidateTaggingTest extends BaseRestIntegrationTest {
    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private AuthenticationTestClient authenticationClient;

    @Autowired
    private CandidateTaggingRepository  candidateTaggingRepository;

    @Autowired
    private TestCandidateFactory candidateFactory;

    private ApiResult tagCandidate(UUID candidateId, TagCandidateCommand command, TestUser user) {

        var token = authenticationClient.login(user);

        return restTestClient.put()
                .uri(url(
                        "/api/recruitment/candidates/%s/tag"
                                .formatted(candidateId)
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ApiResult.class)
                .returnResult()
                .getResponseBody();
    }

    @Test
    void shouldTagCandidate() {
        UUID tagID = UUID.fromString("20000000-0000-0000-0000-000000000017");

        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var candidate = candidateFactory.create(organization.id(), "john.connor@sky.net");

        var command = new TagCandidateCommand(tagID);

        var response = tagCandidate(candidate.id(), command, recruiter);

        // then
        assertThat(response).isNotNull();

        var tags = candidateTaggingRepository.getListOfTags(new CandidateId(candidate.id()));

        assertThat(tags)
                .isNotNull()
                .hasSize(1);

        var tag = tags.getFirst();

        assertThat(tag.name()).isEqualTo("Spring Boot");
    }

    @Test
    void whenTagNotExitsReturn404() {
        UUID tagID = UUID.randomUUID();

        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var candidate = candidateFactory.create(organization.id(), "alex.smith@sky.net");

        var command = new TagCandidateCommand(tagID);

        var token = authenticationClient.login(recruiter);

        restTestClient.put()
                .uri(url(
                        "/api/recruitment/candidates/%s/tag"
                                .formatted(candidate.id())
                ))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .body(command)
                .exchange()
                .expectStatus()
                .isNotFound()
                .returnResult();
    }

    @Test
    void shouldTagAndRemoveTagForCandidate() {
        UUID tagID = UUID.fromString("20000000-0000-0000-0000-000000000017");

        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var candidate = candidateFactory.create(organization.id(), "john.connor@sky.net");

        var command = new TagCandidateCommand(tagID);

        tagCandidate(candidate.id(), command, recruiter);

        var token = authenticationClient.login(recruiter);

        restTestClient.delete()
                .uri(url(
                        "/api/recruitment/candidates/%s/tag/%s"
                                .formatted(candidate.id(), tagID)
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult();
    }

    @Test
    void shouldTagCandidateOnlyOnce() {
        UUID tagID = UUID.fromString("20000000-0000-0000-0000-000000000017");

        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var candidate = candidateFactory.create(organization.id(), "john.connor@sky.net");

        var command = new TagCandidateCommand(tagID);

        var response = tagCandidate(candidate.id(), command, recruiter);

        // then
        assertThat(response).isNotNull();


        tagCandidate(candidate.id(), command, recruiter);


        var tags = candidateTaggingRepository.getListOfTags(new CandidateId(candidate.id()));

        assertThat(tags)
                .isNotNull()
                .hasSize(1);

        var tag = tags.getFirst();

        assertThat(tag.name()).isEqualTo("Spring Boot");
    }

    @Test
    void shouldReturnCandidateTAgs() {
        UUID tagId = UUID.fromString("20000000-0000-0000-0000-000000000001");
        UUID tagId2 = UUID.fromString("20000000-0000-0000-0000-000000000007");
        UUID tagId3 = UUID.fromString("40000000-0000-0000-0000-000000000005");
        UUID tagId4 = UUID.fromString("20000000-0000-0000-0000-000000000015");
        UUID tagId5 = UUID.fromString("20000000-0000-0000-0000-000000000020");

        var tags = List.of(tagId, tagId2, tagId3, tagId4, tagId5);

        var organization = organizationFactory.create();

        var recruiter = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var candidate = candidateFactory.create(organization.id(), "john.connor@sky.net");

        for (var tag : tags) {
            var command = new TagCandidateCommand(tag);
            tagCandidate(candidate.id(), command, recruiter);
        }

        var token = authenticationClient.login(recruiter);

        var response =   restTestClient.get()
                .uri(url(
                        "/api/recruitment/candidates/%s/tag"
                                .formatted(candidate.id())
                ))
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<CandidateTagItem>>() {
                })
                .returnResult().getResponseBody();

        assertThat(response)
                .isNotNull()
                .hasSize(5);

    }

}
