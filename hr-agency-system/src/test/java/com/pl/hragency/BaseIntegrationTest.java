package com.pl.hragency;

import com.pl.hragency.audit.adapter.persistence.AuditJpaEntity;
import com.pl.hragency.audit.adapter.persistence.SpringDataAuditRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
@AutoConfigureRestTestClient
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

    @ServiceConnection
    protected static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("hr_app")
                    .withUsername("hr_app")
                    .withPassword("hr_app");

    @BeforeAll
    static void startContainer() {
        POSTGRES.start();
    }

    @Autowired
    private SpringDataAuditRepository auditRepository;

    protected List<AuditJpaEntity> awaitAuditEntries(
            String aggregateType,
            UUID aggregateId,
            int expectedSize
    ) {
        AtomicReference<List<AuditJpaEntity>> result =
                new AtomicReference<>();

        await()
                .atMost(Duration.ofSeconds(5))
                .until(() -> {
                    var entries =
                            auditRepository
                                    .findByAggregateTypeAndAggregateIdOrderByOccurredAtAsc(
                                            aggregateType,
                                            aggregateId
                                    );

                    result.set(entries);

                    return entries.size() == expectedSize;
                });

        return result.get();
    }
}
