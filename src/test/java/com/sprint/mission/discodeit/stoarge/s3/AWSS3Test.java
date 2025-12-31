package com.sprint.mission.discodeit.stoarge.s3;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
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
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
public class AWSS3Test {

  private S3Client s3Client;
  private S3Presigner s3Presigner;
  private String bucketName;

  @BeforeEach
  void setUp() throws IOException {

    Properties properties = new Properties();
    InputStream inputStream = Files.newInputStream(Path.of(".env"));
    properties.load(inputStream);

    String accessKey = properties.getProperty("AWS_S3_ACCESS_KEY");
    String secretKey = properties.getProperty("AWS_S3_SECRET_KEY");
    String region = properties.getProperty("AWS_S3_REGION");
    bucketName = properties.getProperty("AWS_S3_BUCKET");

    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

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
  @DisplayName("S3 업로드")
  void upload() {

    String testContent = "S3 Test";
    byte[] testBytes = testContent.getBytes();
    String filename = "test-" + UUID.randomUUID() + ".txt";

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(filename)
        .contentType("text/plain")
        .build();

    assertDoesNotThrow(() -> {
      s3Client.putObject(request, RequestBody.fromBytes(testBytes));
    });
    log.info("업로드 성공 " + filename);
  }

  @Test
  @DisplayName("S3 다운로드")
  void download() {

    String testContent = "S3 Test";
    byte[] testBytes = testContent.getBytes();
    String filename = "test-" + UUID.randomUUID() + ".txt";

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(filename)
        .contentType("text/plain")
        .build();

    s3Client.putObject(request, RequestBody.fromBytes(testBytes));

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(filename)
        .build();

    ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(getObjectRequest);

    byte[] byteArray = response.asByteArray();
    String result = new String(byteArray);

    assertEquals(testContent, result);
    log.info("다운로드 성공");
  }

  @Test
  @DisplayName("presignedUrl 생성")
  void presignedUrl_Create() {

    String testContent = "S3 Test";
    byte[] testBytes = testContent.getBytes();
    String filename = "test-" + UUID.randomUUID() + ".txt";

    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(filename)
        .contentType("text/plain")
        .build();

    s3Client.putObject(request, RequestBody.fromBytes(testBytes));

    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(bucketName)
        .key(filename)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(1))
        .getObjectRequest(getObjectRequest)
        .build();

    PresignedGetObjectRequest presignedGetObjectRequest =
        s3Presigner.presignGetObject(presignRequest);

    String url = presignedGetObjectRequest.url().toString();

    log.info("presignedUrl 생성: {}", url);
  }


}
