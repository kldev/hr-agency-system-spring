package com.pl.hragency.recruitment.feeds.handler;

import com.pl.hragency.organization.api.OrganizationApi;
import com.pl.hragency.recruitment.feeds.domain.model.FeedType;
import com.pl.hragency.shared.rest.EntityNotFoundException;
import com.pl.hragency.shared.rest.EntityType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class GetFeedFileHandler {
    private final OrganizationApi api;
    private final S3Client s3Client;
    private final String bucketName;

    public GetFeedFileHandler(@Value("${rustfs.feeds.bucket:jobs-feed}")
                              String bucketName,
                              OrganizationApi api,
                              S3Client s3Client) {
        this.bucketName = bucketName;
        this.api = api;
        this.s3Client = s3Client;
    }

    public ResponseEntity<StreamingResponseBody> handle(String slug, FeedType type) {
        var organization = api.findBySlug(slug);

        if (organization == null) {
            throw new EntityNotFoundException(EntityType.Organization, slug);
        }

        String key = organization.id() + "/jobs." + type.name();

        var request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key).build();

        try {
            var inputStream = s3Client.getObject(request);

            StreamingResponseBody body = outputStream -> {
                try (inputStream) {
                    inputStream.transferTo(outputStream);
                }
            };

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_XML)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"jobs.%s\"".formatted(type.name())
                    )
                    .body(body);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new EntityNotFoundException(
                        EntityType.JobFeed,
                        slug
                );
            }

            throw new IllegalStateException(
                    "Failed to retrieve job feed",
                    e
            );
        }
    }
}
