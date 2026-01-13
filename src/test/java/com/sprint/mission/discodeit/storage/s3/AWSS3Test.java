package com.sprint.mission.discodeit.storage.s3;

import org.junit.jupiter.api.*;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled("CI 환경에서는 S3 통합 테스트를 실행하지 않는다, .env 이슈")
public class AWSS3Test {

    private static final String TEST_CONTENT = "hello s3";

    private static Properties properties;

    private S3Client s3Client;
    private S3Presigner presigner;

    private String bucket;
    private String region;

    // 저장될 파일명(key)
    private String uploadedKey;


    @BeforeAll
    void setUp() throws IOException {
        properties = new Properties();
        properties.load(new FileInputStream(".env"));

        String accessKey = properties.getProperty("AWS_ACCESS_KEY_ID");
        String secretKey = properties.getProperty("AWS_SECRET_ACCESS_KEY");

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        region = properties.getProperty("AWS_S3_REGION");
        bucket = properties.getProperty("AWS_S3_BUCKET");

        s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

        presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();

    }

    @AfterAll
    void tearDown() {
        if (presigner != null) presigner.close();
        if (s3Client != null) s3Client.close();
    }

    @Test
    void uploadTest() {
        uploadedKey = "test/" + UUID.randomUUID() + ".txt";
        byte[] data = TEST_CONTENT.getBytes(StandardCharsets.UTF_8);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uploadedKey)
                .contentType("text/plain")
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(data));

        // 업로드 되었는지 HEAD 검증
        HeadObjectResponse headResponse = s3Client.headObject(builder -> builder.bucket(bucket).key(uploadedKey));
        assertNotNull(headResponse);
        assertEquals(headResponse.contentLength(), data.length);
        assertTrue(headResponse.contentLength() > 0);
        assertEquals("text/plain", headResponse.contentType());
    }

    @Test
    void downloadTest() {
        if (uploadedKey == null) uploadTest();
        GetObjectRequest getRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(uploadedKey)
                .build();

        ResponseBytes<GetObjectResponse> responseBytes = s3Client.getObjectAsBytes(getRequest);
        String text = responseBytes.asString(StandardCharsets.UTF_8);
        assertEquals(TEST_CONTENT, text);
        assertNotNull(responseBytes);
        assertEquals(TEST_CONTENT.getBytes(StandardCharsets.UTF_8).length, responseBytes.asByteArray().length);
        assertTrue(responseBytes.asByteArray().length > 0);
    }

    @Test
    void presignedUrlTest() {
        String key = "presigned/" + UUID.randomUUID() + ".txt";
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("text/plain")
                .build();

        PutObjectPresignRequest presignedRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5))
                .putObjectRequest(putRequest)
                .build();

        String url = presigner.presignPutObject(presignedRequest).url().toString();
        assertNotNull(url);
        assertTrue(url.contains(bucket));
        assertTrue(url.contains(key));
        assertTrue(url.contains("X-Amz-Algorithm"));
        assertTrue(url.contains("X-Amz-Credential"));
        assertTrue(url.contains("X-Amz-Date"));
        assertTrue(url.contains("X-Amz-Expires"));
        assertTrue(url.contains("X-Amz-SignedHeaders"));
        assertTrue(url.contains("X-Amz-Signature"));

    }
}
