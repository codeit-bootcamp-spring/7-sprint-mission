package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("AWS S3 Test")
public class AWSS3Test {

    private static final Logger log = LoggerFactory.getLogger(AWSS3Test.class);
    private static String accessKey;
    private static String secretKey;
    private static String bucketName;
    private static String region;
    private static S3Client s3Client;
    private static S3Presigner s3Presigner;

    // S3Client처럼 생성 비용이 큰 객체는 @BeforeAll + static 사용 권장
    @BeforeAll
    static void setUp() throws IOException {
        Properties properties = new Properties();
        File envFile = new File(".env");

        if (!envFile.exists()) {
            throw new FileNotFoundException(envFile.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(envFile)) {
            properties.load(fis);
        }

        accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
        secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
        region = properties.getProperty("AWS_S3_REGION");
        bucketName = properties.getProperty("AWS_S3_BUCKET");

        assertThat(accessKey).isNotNull();
        assertThat(secretKey).isNotNull();
        assertThat(region).isNotNull();
        assertThat(bucketName).isNotNull();

        assertThat(accessKey).isNotBlank();
        assertThat(secretKey).isNotBlank();
        assertThat(region).isNotBlank();
        assertThat(bucketName).isNotBlank();

        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKey, secretKey);

        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();

        s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .build();
    }

    @Test
    @DisplayName("업로드 테스트")
    void testUpload() throws IOException {
        String testFileName = "test-upload" + UUID.randomUUID() + ".txt";
        String testContent = "AWS S3 test upload";

        // S3에 업로드
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(testFileName)
                .contentType("text/plain")
                .build();

        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromString(testContent)
        );

        assertThat(response).isNotNull();

        log.info("response = {}", response);

    }

    @Test
    @DisplayName("다운로드 테스트")
    void testDownload() throws IOException {
        String testFileName = "test-download" + UUID.randomUUID() + ".txt";
        String testContent = "AWS S3 test download";

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(testFileName)
                        .build(),

                RequestBody.fromString(testContent)
        );

        // 다운로드
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(testFileName)
                .build();

        ResponseInputStream<GetObjectResponse> response = s3Client.getObject(getObjectRequest);

        // 내용 검증
        String downloadedContent = new String(response.readAllBytes());
        assertThat(downloadedContent).isEqualTo(testContent);

        log.info("downloadedContent = {}", downloadedContent);

        // 정리
        response.close();
    }

    @Test
    @DisplayName("PresignedUrl 생성 테스트")
    void testGeneratePresignedUrl() {
        // 먼저 파일 업로드
        String testFileName = "test-presigned-" + UUID.randomUUID() + ".txt";
        String testContent = "Presigned URL test";

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(testFileName)
                        .build(),
                RequestBody.fromString(testContent)
        );

        // Presigned URL 생성 (10분 유효)
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(testFileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        String presignedUrl = presignedRequest.url().toString();

        assertThat(presignedUrl).isNotNull();
        assertThat(presignedUrl.contains(bucketName));
        assertThat(presignedUrl.contains(testFileName));

        log.info(presignedUrl);
    }


}
