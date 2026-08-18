package com.pl.hragency.recruitment.adapter.persistence;

import com.pl.hragency.BaseIntegrationTest;
import com.pl.hragency.identity.domain.model.OrganizationRole;
import com.pl.hragency.jobdescription.api.EmploymentType;
import com.pl.hragency.jobdescription.api.SalaryRange;
import com.pl.hragency.jobdescription.api.WorkMode;
import com.pl.hragency.recruitment.adapter.persistence.posting.JobPostingPersistenceAdapter;
import com.pl.hragency.recruitment.adapter.persistence.posting.SpringDataJobPostingRepository;
import com.pl.hragency.recruitment.domain.model.posting.JobPosting;
import com.pl.hragency.recruitment.domain.model.posting.JobPostingStatus;
import com.pl.hragency.testsupport.TestCompanyFactory;
import com.pl.hragency.testsupport.TestJobDescriptionFactory;
import com.pl.hragency.testsupport.TestOrganizationFactory;
import com.pl.hragency.testsupport.TestUserFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class JobPostingPersistenceAdapterTest extends BaseIntegrationTest {

    @Autowired
    private TestOrganizationFactory organizationFactory;

    @Autowired
    private TestUserFactory userFactory;

    @Autowired
    private TestCompanyFactory companyFactory;

    @Autowired
    private TestJobDescriptionFactory jobDescriptionFactory;

    @Autowired
    private JobPostingPersistenceAdapter adapter;

    private JobPosting createJobPosting() {

        var organization = organizationFactory.create();
        var user = userFactory.create(
                organization,
                "recruiter@test.com",
                "Password123!",
                OrganizationRole.RECRUITER
        );

        var companyId = companyFactory.create(
                organization.id()
        );

        var jobDescriptionId = jobDescriptionFactory.create(organization.id(), companyId, user.id());


        return JobPosting.draft(organization.id(),
                jobDescriptionId,
                companyId,
                user.id(),
                "Java Developer",
                "Java Developer for recruitment project",
                "We are looking for an experienced Java Developer.",
                java.util.List.of("Develop backend applications",
                        "Participate in code reviews",
                        "Cooperate with frontend developers"),
                java.util.List.of("3+ years of Java experience",
                        "Spring Boot experience",
                        "Good English"),
                java.util.List.of("Java",
                        "Spring Boot",
                        "PostgreSQL",
                        "Docker"),
                "Opole",
                "PL",
                EmploymentType.FULL_TIME,
                WorkMode.HYBRID,
                new SalaryRange(new BigDecimal("12000.00"),
                        new BigDecimal("18000.00"),
                        Currency.getInstance("PLN")),
                "java-developer-332a",
                organization.slug()

        );
    }

    @Test
    void shouldSaveAndFindJobPosting() {
        JobPosting jobPosting = createJobPosting();

        adapter.create(jobPosting);

        Optional<JobPosting> result =
                adapter.findById(
                        jobPosting.organizationId(),
                        jobPosting.id()
                );

        assertThat(result)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "version")
                .isEqualTo(jobPosting);

        JobPosting persisted = result.get();

        assertThat(persisted.createdAt())
                .isCloseTo(jobPosting.createdAt(), within(1, ChronoUnit.MICROS));

    }

    @Test
    void shouldNotFindJobPostingFromAnotherOrganization() {
        JobPosting jobPosting = createJobPosting();

        adapter.create(jobPosting);

        var anotherOrganization = organizationFactory.create();

        var result = adapter.findById(
                anotherOrganization.id(),
                jobPosting.id()
        );

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFindJobPostingBySlug() {
        JobPosting jobPosting = createJobPosting();

        adapter.create(jobPosting);

        var result = adapter.findBySlug(
                jobPosting.organizationId(),
                jobPosting.slug()
        );

        assertThat(result)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt", "version")
                .isEqualTo(jobPosting);
    }

    @Test
    void shouldUpdateJobPosting() {
        // given
        var jobPosting = createJobPosting();

        adapter.create(jobPosting);

        var persisted = adapter.findById(
                jobPosting.organizationId(),
                jobPosting.id()
        ).orElseThrow();

        var originalCreatedAt = persisted.createdAt();

        // when
        persisted.updateContent("New title", "New summary", "New description",
                List.of("res1", "res2"),
                List.of("reg 1", "reg 2"),
                List.of("skill 1", "skill 2"),
                "NewLocation",
                "DE",
                EmploymentType.PART_TIME,
                WorkMode.ON_SITE,
                new SalaryRange(new BigDecimal("12300.00"),
                        new BigDecimal("15000.00"),
                        Currency.getInstance("PLN"))
        );
        persisted.updateStatus(JobPostingStatus.PUBLISHED);
        adapter.update(persisted);

        // then
        // then
        var result = adapter.findById(
                jobPosting.organizationId(),
                jobPosting.id()
        ).orElseThrow();

        assertThat(result.status())
                .isEqualTo(JobPostingStatus.PUBLISHED);

        assertThat(result.title())
                .isEqualTo("New title");

        assertThat(result.summary())
                .isEqualTo("New summary");

        assertThat(result.description())
                .isEqualTo("New description");

        assertThat(result.responsibilities())
                .containsExactly("res1", "res2");

        assertThat(result.requirements())
                .containsExactly("reg 1", "reg 2");

        assertThat(result.skills())
                .containsExactly("skill 1", "skill 2");

        assertThat(result.location())
                .isEqualTo("NewLocation");

        assertThat(result.countryCode())
                .isEqualTo("DE");

        assertThat(result.employmentType())
                .isEqualTo(EmploymentType.PART_TIME);

        assertThat(result.workMode())
                .isEqualTo(WorkMode.ON_SITE);

        assertThat(result.salaryRange())
                .isEqualTo(new SalaryRange(
                        new BigDecimal("12300.00"),
                        new BigDecimal("15000.00"),
                        Currency.getInstance("PLN")
                ));

        assertThat(result.createdAt())
                .isCloseTo(originalCreatedAt, within(1, ChronoUnit.MICROS));
    }
}
