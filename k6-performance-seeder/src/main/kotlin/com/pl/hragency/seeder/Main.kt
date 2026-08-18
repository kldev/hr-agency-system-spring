package com.pl.hragency.seeder

import java.sql.Connection
import java.sql.DriverManager

fun main() {
    val config = SeederConfig.fromEnvironment()

    println("k6 performance database seeder")
    println("JDBC: ${config.jdbcUrl}")
    println("organizations=${config.organizations}")
    println("companiesPerOrganization=${config.companiesPerOrganization}")
    println("jobDescriptionsPerOrganization=${config.jobDescriptionsPerOrganization}")
    println("postingsPerOrganization=${config.postingsPerOrganization}")
    println("candidatesPerOrganization=${config.candidatesPerOrganization}")
    println("applicationsPerOrganization=${config.applicationsPerOrganization}")
    println("interviewsPerOrganization=${config.interviewsPerOrganization}")

    DriverManager.getConnection(
        config.jdbcUrl,
        config.username,
        config.password
    ).use { connection ->
        connection.autoCommit = false

        val seeder = DatabaseSeeder(connection, config)
        seeder.seed()

        connection.commit()
    }

    println("Seeding completed.")
}
