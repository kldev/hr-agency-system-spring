package com.pl.hragency.seeder

import java.sql.Connection
import java.sql.PreparedStatement
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.UUID

class DatabaseSeeder(
    connection: Connection,
    private val config: SeederConfig
) {
    private val copy = StreamingCopy(connection)

    fun seed() {
        seedOrganizations()
        seedCompanies()
        seedUsers()
        seedJobDescriptions()
        seedJobPostings()
        seedCandidates()
        seedApplications()
        seedCandidateTags()
        seedApplicationNotes()
        seedInterviews()

        println("All tables seeded.")
    }

    private fun seedOrganizations() {
        copy.copy(
            "organizations",
            "id,name,slug,created_at",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val id = Ids.of("organization", config.batchSeed, org)
                        yield(
                            Csv.row(
                                id,
                                "Performance Organization ${org + 1}",
                                "performance-org-${org + 1}",
                                Csv.now(config.batchSeed, org.toLong())
                            )
                        )
                    }
                }
            )
        )
        println("organizations copied")
    }

    private fun seedCompanies() {
        copy.copy(
            "companies",
            "id,organization_id,name,tax_id,country_code,city,address,postal_code,registration_number,status,sales_owner_id,created_at",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)
                        repeat(config.companiesPerOrganization) { company ->
                            val id = Ids.of("company", config.batchSeed, org, company)
                            yield(
                                Csv.row(
                                    id,
                                    organizationId,
                                    "Performance Company ${org + 1}-${company + 1}",
                                    "TAX-${org + 1}-${company + 1}",
                                    "PL",
                                    "Opole",
                                    "Test Street ${company + 1}",
                                    "45-${100 + company}",
                                    "REG-${org + 1}-${company + 1}",
                                    "ACTIVE",
                                    null,
                                    Csv.now(config.batchSeed, org * 100000L + company)
                                )
                            )
                        }
                    }
                }
            )
        )
        println("companies copied")
    }

    private fun seedUsers() {
        copy.copy(
            "users",
            "id,organization_id,email,first_name,last_name,role,created_at,password_hash",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)

                        val recruiterId = Ids.of("user", config.batchSeed, org, "recruiter")
                        yield(
                            Csv.row(
                                recruiterId,
                                organizationId,
                                "recruiter${org + 1}@performance.test",
                                "Recruiter",
                                "${org + 1}",
                                "RECRUITER",
                                Csv.now(config.batchSeed, org * 10L),
                                config.passwordHash
                            )
                        )

                        val adminId = Ids.of("user", config.batchSeed, org, "admin")
                        yield(
                            Csv.row(
                                adminId,
                                organizationId,
                                "admin${org + 1}@performance.test",
                                "Admin",
                                "${org + 1}",
                                "ADMIN",
                                Csv.now(config.batchSeed, org * 10L + 1),
                                config.passwordHash
                            )
                        )
                    }
                }
            )
        )
        println("users copied")
    }

    private fun seedJobDescriptions() {
        copy.copy(
            "job_descriptions",
            "id,organization_id,company_id,recruiter_id,title,summary,description,responsibilities,requirements,skills,location,country_code,employment_type,work_mode,salary_min,salary_max,salary_currency,status,created_at,updated_at",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)
                        val recruiterId = Ids.of("user", config.batchSeed, org, "recruiter")

                        repeat(config.jobDescriptionsPerOrganization) { jd ->
                            val id = Ids.of("job-description", config.batchSeed, org, jd)
                            val companyId = Ids.of(
                                "company",
                                config.batchSeed,
                                org,
                                jd % config.companiesPerOrganization
                            )

                            yield(
                                Csv.row(
                                    id,
                                    organizationId,
                                    companyId,
                                    recruiterId,
                                    "Backend Developer ${jd + 1}",
                                    "Backend Developer performance test position",
                                    "Backend development position generated for k6 performance testing.",
                                    Csv.jsonArray("Develop backend services", "Review code", "Write tests"),
                                    Csv.jsonArray("Java", "Spring Boot", "PostgreSQL"),
                                    Csv.jsonArray("JAVA", "SPRING_BOOT", "POSTGRESQL"),
                                    "Opole",
                                    "PL",
                                    if (jd % 3 == 0) "FULL_TIME" else "CONTRACT",
                                    if (jd % 3 == 0) "HYBRID" else "REMOTE",
                                    12000.00,
                                    22000.00,
                                    "PLN",
                                    "ACTIVE",
                                    Csv.now(config.batchSeed, org * 100000L + jd),
                                    Csv.now(config.batchSeed, org * 100000L + jd + 1)
                                )
                            )
                        }
                    }
                }
            )
        )
        println("job_descriptions copied")
    }

    private fun seedJobPostings() {
        copy.copy(
            "job_postings",
            "id,organization_id,job_description_id,company_id,recruiter_id,title,summary,description,responsibilities,requirements,skills,location,country_code,employment_type,work_mode,salary_min,salary_max,salary_currency,status,created_at,updated_at,version,slug,organization_slug",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)
                        val recruiterId = Ids.of("user", config.batchSeed, org, "recruiter")

                        repeat(config.postingsPerOrganization) { posting ->
                            val id = Ids.of("job-posting", config.batchSeed, org, posting)
                            val jd = posting % config.jobDescriptionsPerOrganization
                            val jobDescriptionId = Ids.of("job-description", config.batchSeed, org, jd)
                            val companyId = Ids.of("company", config.batchSeed, org, posting % config.companiesPerOrganization)
                            val slug = "backend-developer-${org + 1}-${posting + 1}"

                            yield(
                                Csv.row(
                                    id,
                                    organizationId,
                                    jobDescriptionId,
                                    companyId,
                                    recruiterId,
                                    "Backend Developer ${posting + 1}",
                                    "Backend Developer performance test position",
                                    "Backend development position generated for k6 performance testing.",
                                    Csv.jsonArray("Develop backend services", "Review code", "Write tests"),
                                    Csv.jsonArray("Java", "Spring Boot", "PostgreSQL"),
                                    Csv.jsonArray("JAVA", "SPRING_BOOT", "POSTGRESQL"),
                                    "Opole",
                                    "PL",
                                    "FULL_TIME",
                                    "HYBRID",
                                    12000.00,
                                    22000.00,
                                    "PLN",
                                    "PUBLISHED",
                                    Csv.now(config.batchSeed, org * 100000L + posting),
                                    Csv.now(config.batchSeed, org * 100000L + posting + 1),
                                    0,
                                    slug,
                                    "performance-org-${org + 1}"
                                )
                            )
                        }
                    }
                }
            )
        )
        println("job_postings copied")
    }

    private fun seedCandidates() {
        copy.copy(
            "candidates",
            "id,organization_id,email,first_name,last_name,phone,summary,status,source,created_at,updated_at,version",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)

                        repeat(config.candidatesPerOrganization) { candidate ->
                            val id = Ids.of("candidate", config.batchSeed, org, candidate)

                            yield(
                                Csv.row(
                                    id,
                                    organizationId,
                                    "candidate-${org + 1}-${candidate + 1}@performance.test",
                                    "Candidate",
                                    "${candidate + 1}",
                                    "+481234${String.format("%04d", candidate % 10000)}",
                                    "Performance test candidate",
                                    "ACTIVE",
                                    "DIRECT",
                                    Csv.now(config.batchSeed, org * 1000000L + candidate),
                                    Csv.now(config.batchSeed, org * 1000000L + candidate + 1),
                                    0
                                )
                            )
                        }
                    }
                }
            )
        )
        println("candidates copied")
    }

   private fun seedApplications() {
        require(
            config.applicationsPerOrganization <=
                config.candidatesPerOrganization * config.postingsPerOrganization
        ) {
            "applicationsPerOrganization cannot exceed " +
                "candidatesPerOrganization * postingsPerOrganization"
        }

        copy.copy(
            "applications",
            "id,organization_id,candidate_id,job_posting_id,source,status,created_at,updated_at",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId =
                            Ids.of("organization", config.batchSeed, org)

                        repeat(config.applicationsPerOrganization) { application ->
                            val candidateIndex =
                                application % config.candidatesPerOrganization

                            val postingIndex =
                                application / config.candidatesPerOrganization

                            val id =
                                Ids.of("application", config.batchSeed, org, application)

                            val candidateId =
                                Ids.of("candidate", config.batchSeed, org, candidateIndex)

                            val postingId =
                                Ids.of("job-posting", config.batchSeed, org, postingIndex)

                            yield(
                                Csv.row(
                                    id,
                                    organizationId,
                                    candidateId,
                                    postingId,
                                    if (application % 2 == 0) "CAREER_PAGE" else "DIRECT",
                                    if (application % 5 == 0) "HIRED" else "IN_REVIEW",
                                    Csv.now(
                                        config.batchSeed,
                                        org * 1_000_000L + application
                                    ),
                                    Csv.now(
                                        config.batchSeed,
                                        org * 1_000_000L + application + 1
                                    )
                                )
                            )
                        }
                    }
                }
            )
        )

        println("applications copied")
    }

    private fun seedCandidateTags() {
        // Tags are reference data and are seeded separately by sql/tags.sql.
        // Candidate tags only reference those stable IDs.
        val tagIds = listOf(
            "20000000-0000-0000-0000-000000000001", // JAVA
            "20000000-0000-0000-0000-000000000002", // C_SHARP
            "20000000-0000-0000-0000-000000000003", // JAVASCRIPT
            "20000000-0000-0000-0000-000000000004", // TYPESCRIPT
            "20000000-0000-0000-0000-000000000005", // PYTHON
            "20000000-0000-0000-0000-000000000006", // PHP
            "20000000-0000-0000-0000-000000000007", // KOTLIN
            "20000000-0000-0000-0000-000000000008", // SQL
            "20000000-0000-0000-0000-000000000009", // POSTGRESQL
            "20000000-0000-0000-0000-000000000010", // MYSQL
            "20000000-0000-0000-0000-000000000011", // DOCKER
            "20000000-0000-0000-0000-000000000012", // KUBERNETES
            "20000000-0000-0000-0000-000000000013", // AWS
            "20000000-0000-0000-0000-000000000014", // AZURE
            "20000000-0000-0000-0000-000000000015", // GIT
            "20000000-0000-0000-0000-000000000016", // LINUX
            "20000000-0000-0000-0000-000000000017", // SPRING_BOOT
            "20000000-0000-0000-0000-000000000018", // REACT
            "20000000-0000-0000-0000-000000000019", // ANGULAR
            "20000000-0000-0000-0000-000000000020"  // DOTNET
        )

        copy.copy(
            "candidate_tags",
            "candidate_id,tag_id,created_at",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        repeat(config.candidatesPerOrganization) { candidate ->
                            val candidateId = Ids.of("candidate", config.batchSeed, org, candidate)
                            repeat(minOf(config.candidateTagsPerCandidate, tagIds.size)) { tag ->
                                yield(
                                    Csv.row(
                                        candidateId,
                                        tagIds[(candidate + tag) % tagIds.size],
                                        Csv.now(config.batchSeed, org * 1000000L + candidate * 10L + tag)
                                    )
                                )
                            }
                        }
                    }
                }
            )
        )
        println("candidate_tags copied")
    }

    private fun seedApplicationNotes() {
        if (config.notesPerApplication <= 0) return

        copy.copy(
            "application_notes",
            "id,organization_id,application_id,author_id,content,created_at",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)
                        val recruiterId = Ids.of("user", config.batchSeed, org, "recruiter")

                        repeat(config.applicationsPerOrganization) { application ->
                            repeat(config.notesPerApplication) { note ->
                                val applicationId = Ids.of("application", config.batchSeed, org, application)

                                yield(
                                    Csv.row(
                                        Ids.of("application-note", config.batchSeed, org, application, note),
                                        organizationId,
                                        applicationId,
                                        recruiterId,
                                        "Performance test note $note for application $application",
                                        Csv.now(config.batchSeed, org * 1000000L + application * 10L + note)
                                    )
                                )
                            }
                        }
                    }
                }
            )
        )
        println("application_notes copied")
    }

    private fun seedInterviews() {
        copy.copy(
            "interviews",
            "id,organization_id,candidate_id,application_id,feedback,status,scheduled_at,created_at,updated_at,created_by",
            GeneratorReader(
                sequence {
                    repeat(config.organizations) { org ->
                        val organizationId = Ids.of("organization", config.batchSeed, org)
                        val recruiterId = Ids.of("user", config.batchSeed, org, "recruiter")

                        repeat(config.interviewsPerOrganization) { interview ->
                            val applicationIndex = interview % config.applicationsPerOrganization
                            val candidateIndex = applicationIndex % config.candidatesPerOrganization

                            yield(
                                Csv.row(
                                    Ids.of("interview", config.batchSeed, org, interview),
                                    organizationId,
                                    Ids.of("candidate", config.batchSeed, org, candidateIndex),
                                    Ids.of("application", config.batchSeed, org, applicationIndex),
                                    if (interview % 3 == 0) "Good technical interview" else null,
                                    if (interview % 4 == 0) "COMPLETED" else "PLANNED",
                                    Csv.now(config.batchSeed, org * 1000000L + interview + 500000),
                                    Csv.now(config.batchSeed, org * 1000000L + interview),
                                    Csv.now(config.batchSeed, org * 1000000L + interview + 1),
                                    recruiterId
                                )
                            )
                        }
                    }
                }
            )
        )
        println("interviews copied")
    }
}
