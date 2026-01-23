package com.sprint.mission.discodeit.storage.s3;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AWSS3Test {
    @Test
    @DisplayName("업로드합니다")
    void upload() {
        Properties prop = loadEnv();
        String bucket = s3Check(prop, "AWS_S3_BUCKET");
        String key = "awss3-test/" + UUID.randomUUID() + ".txt";
        byte[] data = ("upload test - " + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);

        try(S3Client s3 = createS3(prop)) {
            s3.headBucket(HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build());

            PutObjectResponse putObjectResponse = s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType("text/plain")
                            .build(),
                    RequestBody.fromBytes(data)
            );
            assertNotNull(putObjectResponse.eTag());
        } finally {
            try (S3Client s3 = createS3(prop)) {
                s3.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
            } catch (Exception e) {
            }
        }
    }

    @Test
    @DisplayName("다운로드입니다.")
    void download() {
        Properties prop = loadEnv();
        String bucket = s3Check(prop, "AWS_S3_BUCKET");
        String key = "awss3-test/" + UUID.randomUUID() + ".txt";
        byte[] data = ("download test - " + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);

        try (S3Client s3 = createS3(prop)) {

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType("text/plain")
                            .build(),
                    RequestBody.fromBytes(data)
            );

            ResponseBytes<GetObjectResponse> objectAsBytes = s3.getObjectAsBytes(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build()
            );

            assertArrayEquals(data, objectAsBytes.asByteArray());
        } finally {
            try (S3Client s3 = createS3(prop)) {
                s3.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
            } catch (Exception e) {}
        }
    }

    @Test
    @DisplayName("Presigned Url 입니다.")
    void presignedUrl() {
        Properties prop = loadEnv();
        String bucket = s3Check(prop, "AWS_S3_BUCKET");
        String key = "awss3-test/" + UUID.randomUUID() + ".txt";
        byte[] data = ("presign test - " + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);

        try (S3Client s3 = createS3(prop);
                S3Presigner s3Presigner = createPresigner(prop)){

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType("text/plain")
                            .build(),
                    RequestBody.fromBytes(data)
            );

            PresignedGetObjectRequest presign = s3Presigner.presignGetObject(
                    GetObjectPresignRequest.builder()
                            .signatureDuration(Duration.ofMinutes(10))
                            .getObjectRequest(
                                    GetObjectRequest.builder()
                                            .bucket(bucket)
                                            .key(key)
                                            .build()
                            )
                            .build()
            );

            assertNotNull(presign.url());
        } finally {
            try (S3Client s3 = createS3(prop)) {
                s3.deleteObject(DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build());
            } catch (Exception e) {
            }
        }
    }

    private Properties loadEnv() {
        Path envPath = Path.of(".env");
        if (!Files.exists(envPath)) {
            throw new IllegalStateException(".env not found");
        }

        Properties prop = new Properties();
        try(BufferedReader br = Files.newBufferedReader(envPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                int index = line.indexOf('=');
                if (index < 0) continue;

                String key = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();

                if (value.length() >= 2) {
                    char c = value.charAt(0), l = value.charAt(value.length() - 1);
                    if ((c == '"' && l == '"') || (c == '\'' && l == '\'')) {
                        value = value.substring(1, value.length() - 1);
                    }
                }

                prop.setProperty(key, value);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read .env", e);
        }
        return prop;
    }

    private static String s3Check(Properties prop, String key) {
        String v = prop.getProperty(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("key is null or blank");
        }
        return v.trim();
    }

    private S3Client createS3(Properties prop) {
        String accessKey = s3Check(prop, "AWS_S3_ACCESS_KEY");
        String secretKey = s3Check(prop, "AWS_S3_SECRET_KEY");
        String region = s3Check(prop, "AWS_S3_REGION");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    private S3Presigner createPresigner(Properties prop) {
        String accessKey = s3Check(prop, "AWS_S3_ACCESS_KEY");
        String secretKey = s3Check(prop, "AWS_S3_SECRET_KEY");
        String region = s3Check(prop, "AWS_S3_REGION");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}
