package com.pl.hragency.recruitment.feeds.task;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pl.hragency.recruitment.feeds.application.JobFeedGenerator;
import com.pl.hragency.recruitment.feeds.domain.model.JobFeedTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
public class JobFeedTaskProcessor {

    private static final Logger logger =
            LoggerFactory.getLogger(JobFeedTaskProcessor.class);

    private final String bucketName;
    private final S3Client s3Client;
    private final JobFeedGenerator generator;

    public JobFeedTaskProcessor(
            @Value("${rustfs.bucket:jobs-feed}") String bucketName,
            S3Client s3Client,
            JobFeedGenerator generator
    ) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.generator = generator;
    }

    public void process(JobFeedTask task) throws JsonProcessingException {

        logger.info(
                "Processing job feed task id: {} organization id: {}",
                task.id(),
                task.organizationId()
        );

        ensureBucketExists();

        var generateResult = generator.generate(task.organizationId());

        String key = task.organizationId() + "/jobs.xml";

        var request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType("application/xml")
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(generateResult.xml()));

        String jsonKey = task.organizationId() + "/jobs.json";
        var jsonRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(jsonKey)
                        .contentType("application/json")
                        .build();

        s3Client.putObject(
                jsonRequest,
                RequestBody.fromBytes(generateResult.json())
        );
    }

    private void ensureBucketExists() {
        try {
            s3Client.headBucket(
                    HeadBucketRequest.builder()
                            .bucket(bucketName)
                            .build()
            );

        } catch (NoSuchBucketException e) {
            createBucket();
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                createBucket();
                return;
            }

            throw e;
        }
    }

    private void createBucket() {
        logger.info("Creating S3 bucket '{}'", bucketName);

        s3Client.createBucket(
                CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build()
        );
    }
}