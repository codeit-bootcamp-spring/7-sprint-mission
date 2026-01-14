package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.FileInputStream;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AWS_S3 Test")
class AWSS3Test {

    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private String bucketName;

    @BeforeEach
    void setUp() throws Exception {

        String rootPath = System.getProperty("user.dir");
        String envPath = Paths.get(rootPath, ".env").toString();

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(envPath)) {
            props.load(fis);
        }

        String accessKey = props.getProperty("AWS_S3_ACCESS_KEY");
        String secretKey = props.getProperty("AWS_S3_SECRET_KEY");
        String region = props.getProperty("AWS_S3_REGION");
        this.bucketName = props.getProperty("AWS_S3_BUCKET");

        AwsBasicCredentials credentials
                = AwsBasicCredentials.create(accessKey, secretKey);

        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        this.s3Presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Test
    @DisplayName("업로드 및 다운로드 테스트")
    void uploadAndDownloadTest() {
        // given
        String fileName = "test_" + UUID.randomUUID() + "_.txt";
        String content = "hello world!";

        // when upload
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType("text/plain")
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromString(content)
        );
        System.out.println("upload success");

        // when download
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> objectAsBytes
                = s3Client.getObjectAsBytes(getObjectRequest);

        String downContent = new String(objectAsBytes.asByteArray());
        System.out.println("download success");

        // then
        assertThat(downContent).isEqualTo(content);

    }

    @Test
    @DisplayName("PresignedUrl 생성 테스트")
    void generatePresignedUrlTest() {
        // given
        String fileName = "presignedUrlTest.txt";

        // when
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        String url = presignedRequest.url().toString();

        // then
        System.out.println("presignedUrl: " + url);
        assertThat(url).isNotBlank();
    }
}