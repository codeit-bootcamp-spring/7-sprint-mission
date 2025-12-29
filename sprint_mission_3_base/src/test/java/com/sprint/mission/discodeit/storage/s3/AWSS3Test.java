package com.sprint.mission.discodeit.storage.s3;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

public class AWSS3Test {

    private static Properties loadEnv() {
        Properties props = new Properties();
        Path envPath = Paths.get(".env");
        if (Files.exists(envPath)) {
            try (InputStream in = Files.newInputStream(envPath)) {
                props.load(in);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return props;
    }

    private static String getRequired(Properties props, String key) {
        String v = props.getProperty(key);
        if (v == null || v.isBlank()) {
            v = System.getenv(key);
        }
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("Missing required config: " + key);
        }
        return v.trim();
    }

    private static S3Client s3(Properties props) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
                getRequired(props, "AWS_S3_ACCESS_KEY"),
                getRequired(props, "AWS_S3_SECRET_KEY")
        );

        return S3Client.builder()
                .region(Region.of(getRequired(props, "AWS_S3_REGION")))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    private static S3Presigner presigner(Properties props) {
        AwsBasicCredentials creds = AwsBasicCredentials.create(
                getRequired(props, "AWS_S3_ACCESS_KEY"),
                getRequired(props, "AWS_S3_SECRET_KEY")
        );

        return S3Presigner.builder()
                .region(Region.of(getRequired(props, "AWS_S3_REGION")))
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    @Test
    void upload() {
        Properties props = loadEnv();
        String bucket = getRequired(props, "AWS_S3_BUCKET");
        String key = "mission8-test/hello.txt";
        byte[] content = "hello s3".getBytes(StandardCharsets.UTF_8);

        try (S3Client s3 = s3(props)) {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType("text/plain")
                    .build();

            s3.putObject(req, RequestBody.fromBytes(content));
        } catch (S3Exception e) {
            throw new RuntimeException(e.awsErrorDetails().errorMessage(), e);
        }
    }

    @Test
    void download() {
        Properties props = loadEnv();
        String bucket = getRequired(props, "AWS_S3_BUCKET");
        String key = "mission8-test/hello.txt";
        byte[] expected = "hello s3".getBytes(StandardCharsets.UTF_8);

        try (S3Client s3 = s3(props)) {
            ResponseBytes<GetObjectResponse> res = s3.getObject(
                    GetObjectRequest.builder().bucket(bucket).key(key).build(),
                    ResponseTransformer.toBytes()
            );

            Assertions.assertArrayEquals(expected, res.asByteArray());
        } catch (S3Exception e) {
            throw new RuntimeException(e.awsErrorDetails().errorMessage(), e);
        }
    }

    @Test
    void presignedUrl() {
        Properties props = loadEnv();
        String bucket = getRequired(props, "AWS_S3_BUCKET");
        String key = "mission8-test/hello.txt";

        try (S3Presigner presigner = presigner(props)) {
            GetObjectRequest getReq = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(getReq)
                    .build();

            PresignedGetObjectRequest presigned = presigner.presignGetObject(presignReq);

            Assertions.assertNotNull(presigned.url());
            Assertions.assertTrue(presigned.url().toString().startsWith("https://"));
            System.out.println(presigned.url());
        }
    }
}
