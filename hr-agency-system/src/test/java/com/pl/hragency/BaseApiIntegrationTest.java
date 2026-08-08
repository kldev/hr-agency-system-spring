package com.pl.hragency;


import com.pl.hragency.audit.adapter.persistence.AuditJpaEntity;
import com.pl.hragency.audit.adapter.persistence.SpringDataAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.client.RestTestClient;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureRestTestClient
@ActiveProfiles("test")
public abstract class BaseApiIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    protected int port;

    @Autowired
    protected RestTestClient restTestClient;

    @Autowired
    protected JsonMapper jsonMapper;

    protected String url(String path) {
        return "http://localhost:%d%s"
                .formatted(port, path);
    }


}