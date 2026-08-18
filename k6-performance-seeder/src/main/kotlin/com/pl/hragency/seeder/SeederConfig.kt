package com.pl.hragency.seeder

data class SeederConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val organizations: Int,
    val companiesPerOrganization: Int,
    val jobDescriptionsPerOrganization: Int,
    val postingsPerOrganization: Int,
    val candidatesPerOrganization: Int,
    val applicationsPerOrganization: Int,
    val interviewsPerOrganization: Int,
    val notesPerApplication: Int,
    val candidateTagsPerCandidate: Int,
    val batchSeed: Int,
    val passwordHash: String
) {
    companion object {
        fun fromEnvironment(): SeederConfig {
            fun env(name: String, default: String) =
                System.getenv(name)?.takeIf { it.isNotBlank() } ?: default

            fun int(name: String, default: Int) =
                env(name, default.toString()).toInt()

            return SeederConfig(
                jdbcUrl = env("JDBC_URL", "jdbc:postgresql://localhost:5432/hr_app"),
                username = env("DB_USER", "postgres"),
                password = env("DB_PASSWORD", "postgres"),
                organizations = int("ORGANIZATIONS", 10),
                companiesPerOrganization = int("COMPANIES_PER_ORGANIZATION", 10),
                jobDescriptionsPerOrganization = int("JOB_DESCRIPTIONS_PER_ORGANIZATION", 20),
                postingsPerOrganization = int("POSTINGS_PER_ORGANIZATION", 20),
                candidatesPerOrganization = int("CANDIDATES_PER_ORGANIZATION", 1000),
                applicationsPerOrganization = int("APPLICATIONS_PER_ORGANIZATION", 2000),
                interviewsPerOrganization = int("INTERVIEWS_PER_ORGANIZATION", 500),
                notesPerApplication = int("NOTES_PER_APPLICATION", 0),
                candidateTagsPerCandidate = int("CANDIDATE_TAGS_PER_CANDIDATE", 2),
                batchSeed = int("SEED", 20260818),
                // bcrypt hash for "password"; change this to the hash used by your application.
                passwordHash = env(
                    "PASSWORD_HASH",
                    $$"""$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"""
                )
            )
        }
    }
}