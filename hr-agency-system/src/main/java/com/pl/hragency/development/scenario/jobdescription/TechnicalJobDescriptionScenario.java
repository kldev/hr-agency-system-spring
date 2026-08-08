package com.pl.hragency.development.scenario.jobdescription;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.jobdescription.api.CreateJobDescriptionInput;
import com.pl.hragency.jobdescription.api.JobDescriptionApi;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class TechnicalJobDescriptionScenario {

    private final JobDescriptionApi jobDescriptionApi;
    private final CompanyApi companyApi;

    public TechnicalJobDescriptionScenario(
            JobDescriptionApi jobDescriptionApi,
            CompanyApi companyApi
    ) {
        this.jobDescriptionApi = jobDescriptionApi;
        this.companyApi = companyApi;
    }

    public void create(
            UUID organizationId,
            List<UUID> userIds
    ) {
        var companies = companyApi.findAllIds(
                organizationId,
                20
        );

        if (companies.isEmpty() || userIds.isEmpty()) {
            return;
        }

        var companyIndex = 0;
        var userIndex = 0;

        create(
                organizationId,
                userIds.get(++userIndex % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(++companyIndex % companies.size()),
                        "Java Backend Developer",
                        "Senior",
                        "We are looking for an experienced Java Backend Developer to join our engineering team.",
                        List.of(
                                "Java 21",
                                "Spring Boot",
                                "PostgreSQL",
                                "REST API",
                                "Docker",
                                "Git"
                        ),
                        List.of(
                                "Design and develop backend applications",
                                "Develop and maintain REST APIs",
                                "Write unit and integration tests",
                                "Participate in code reviews",
                                "Cooperate with frontend developers",
                                "Monitor and improve application performance"
                        ),
                        List.of(
                                "At least 3 years of experience with Java",
                                "Practical experience with Spring Boot",
                                "Good knowledge of relational databases",
                                "Experience with REST APIs",
                                "Ability to work in a team",
                                "Good English"
                        ),
                        "Opole",
                        "HYBRID",
                        BigDecimal.valueOf(12000),
                        BigDecimal.valueOf(22000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Senior Full Stack Developer",
                        "Senior",
                        "Development of modern business applications using Java, Spring Boot and React.",
                        List.of(
                                "Java",
                                "Spring Boot",
                                "React",
                                "TypeScript",
                                "PostgreSQL",
                                "Docker"
                        ),
                        List.of(
                                "Develop backend and frontend applications",
                                "Design REST APIs",
                                "Implement new business features",
                                "Review pull requests",
                                "Participate in technical discussions",
                                "Maintain application quality"
                        ),
                        List.of(
                                "At least 4 years of commercial software development",
                                "Strong Java knowledge",
                                "Experience with React and TypeScript",
                                "Good understanding of REST architecture",
                                "Knowledge of PostgreSQL",
                                "Ability to work independently"
                        ),
                        "Wrocław",
                        "HYBRID",
                        BigDecimal.valueOf(14000),
                        BigDecimal.valueOf(24000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Frontend Developer",
                        "Mid",
                        "Join a frontend team building modern web applications for international customers.",
                        List.of(
                                "TypeScript",
                                "React",
                                "HTML",
                                "CSS",
                                "REST API",
                                "Git"
                        ),
                        List.of(
                                "Develop modern web applications",
                                "Create reusable React components",
                                "Integrate applications with REST APIs",
                                "Write automated tests",
                                "Optimize application performance",
                                "Cooperate with UX and backend teams"
                        ),
                        List.of(
                                "At least 2 years of experience with React",
                                "Good knowledge of TypeScript",
                                "Good understanding of HTML and CSS",
                                "Experience with REST APIs",
                                "Knowledge of Git",
                                "English at communicative level"
                        ),
                        "Katowice",
                        "REMOTE",
                        BigDecimal.valueOf(10000),
                        BigDecimal.valueOf(18000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "DevOps Engineer",
                        "Senior",
                        "We are looking for a DevOps Engineer responsible for our cloud infrastructure and deployment platforms.",
                        List.of(
                                "Kubernetes",
                                "Docker",
                                "Azure",
                                "Terraform",
                                "CI/CD",
                                "Linux"
                        ),
                        List.of(
                                "Maintain cloud infrastructure",
                                "Develop CI/CD pipelines",
                                "Automate deployment processes",
                                "Monitor production environments",
                                "Improve system reliability",
                                "Cooperate with development teams"
                        ),
                        List.of(
                                "Experience with Kubernetes",
                                "Strong Docker knowledge",
                                "Experience with Azure or another cloud platform",
                                "Knowledge of Terraform",
                                "Experience with CI/CD",
                                "Good Linux knowledge"
                        ),
                        "Warsaw",
                        "HYBRID",
                        BigDecimal.valueOf(15000),
                        BigDecimal.valueOf(25000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "QA Automation Engineer",
                        "Mid",
                        "Development and maintenance of automated tests for web and backend applications.",
                        List.of(
                                "Java",
                                "Selenium",
                                "Playwright",
                                "REST Assured",
                                "JUnit",
                                "CI/CD"
                        ),
                        List.of(
                                "Develop automated tests",
                                "Maintain existing test suites",
                                "Test REST APIs",
                                "Analyze test results",
                                "Report and verify defects",
                                "Cooperate with developers"
                        ),
                        List.of(
                                "At least 2 years of experience in test automation",
                                "Knowledge of Java",
                                "Experience with Selenium or Playwright",
                                "Experience with REST API testing",
                                "Knowledge of JUnit",
                                "Analytical thinking"
                        ),
                        "Poznań",
                        "HYBRID",
                        BigDecimal.valueOf(10000),
                        BigDecimal.valueOf(17000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Business Analyst",
                        "Mid",
                        "Work closely with business stakeholders and development teams to translate business needs into actionable requirements.",
                        List.of(
                                "Requirements analysis",
                                "UML",
                                "BPMN",
                                "SQL",
                                "Jira",
                                "Confluence"
                        ),
                        List.of(
                                "Analyze business requirements",
                                "Prepare functional specifications",
                                "Model business processes",
                                "Cooperate with stakeholders",
                                "Support development teams",
                                "Participate in acceptance testing"
                        ),
                        List.of(
                                "Experience in business analysis",
                                "Knowledge of BPMN or UML",
                                "Good SQL knowledge",
                                "Experience with Jira and Confluence",
                                "Strong communication skills",
                                "Ability to translate business needs into technical requirements"
                        ),
                        "Wrocław",
                        "HYBRID",
                        BigDecimal.valueOf(10000),
                        BigDecimal.valueOf(18000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Data Engineer",
                        "Mid",
                        "Build and maintain data pipelines supporting analytics and business intelligence.",
                        List.of(
                                "Python",
                                "SQL",
                                "PostgreSQL",
                                "Apache Kafka",
                                "ETL",
                                "Airflow"
                        ),
                        List.of(
                                "Build and maintain data pipelines",
                                "Develop ETL processes",
                                "Optimize SQL queries",
                                "Integrate data from multiple sources",
                                "Monitor data quality",
                                "Cooperate with data analysts"
                        ),
                        List.of(
                                "Experience with Python",
                                "Strong SQL knowledge",
                                "Experience with PostgreSQL",
                                "Knowledge of ETL processes",
                                "Experience with Airflow or similar tools",
                                "Analytical thinking"
                        ),
                        "Kraków",
                        "HYBRID",
                        BigDecimal.valueOf(12000),
                        BigDecimal.valueOf(20000)
                )
        );
    }

    private void create(
            UUID organizationId,
            UUID userId,
            CreateJobDescriptionInput input
    ) {
        jobDescriptionApi.create(
                organizationId,
                userId,
                input
        );
    }
}