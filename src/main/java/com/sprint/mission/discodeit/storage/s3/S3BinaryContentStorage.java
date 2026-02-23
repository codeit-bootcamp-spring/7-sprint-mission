package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.event.S3UploadFailEvent;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.common.FileSaveFailedException;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        name = "discodeit.storage.type",
        havingValue = "s3"
)
@Slf4j
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final ApplicationEventPublisher eventPublisher;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    @Value("${discodeit.storage.s3.bucket}")
    private String bucketName;
    @Value("${discodeit.storage.s3.access-key}")
    private String accessKey;
    @Value("${discodeit.storage.s3.secret-key}")
    private String secretKey;
    @Value("${discodeit.storage.s3.region}")
    private String region;

    @Value("${discodeit.storage.s3.presigned-url-expiration}")
    private int expiration;

    @PostConstruct
    private void initializeAmazonS3Client() {

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

    @Retryable(
            retryFor = { S3Exception.class, FileSaveFailedException.class },
            maxAttempts = 3,
            backoff = @Backoff(
                    delay = 1000, // 1초 대기
                    multiplier = 2 // 재시도마다 딜레이를 2배로 증가
            )

    )
    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String uniqueFileName = binaryContentId.toString();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .contentType("application/octet-stream")
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(bytes)
        );

        return binaryContentId;
    }

    @Recover
    public UUID recover(Exception e, UUID binaryContentId, byte[] bytes) {
        eventPublisher.publishEvent(new S3UploadFailEvent(
                binaryContentId,
                ErrorCode.FILE_SAVE_FAILED.getMessage())
        );

        return null;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String fileName = binaryContentId.toString();

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public ResponseEntity<Void> download(BinaryContent binaryContent) {

        String key = binaryContent.getId().toString();
        String contentType = binaryContent.getContentType();

        String presignedUrl = generatePresignedUrl(key, contentType);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(presignedUrl))
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .responseContentType(contentType)
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + key + "\"")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(expiration))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }
}
