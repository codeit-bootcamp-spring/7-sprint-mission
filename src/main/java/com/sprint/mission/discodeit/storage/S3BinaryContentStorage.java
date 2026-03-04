package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {
    private final String bucket;
    private final long seconds;
    private final S3Client s3;
    private final S3Presigner presigner;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public S3BinaryContentStorage(
            @Value("${discodeit.storage.s3.access-key}")
            String accessKey,
            @Value("${discodeit.storage.s3.secret-key}")
            String secretKey,
            @Value("${discodeit.storage.s3.region}")
            String region,
            @Value("${discodeit.storage.s3.bucket}")
            String bucket,
            @Value("${discodeit.storage.s3.presigned-url-expiration:600}")
            long seconds,
            NotificationService notificationService,
            UserRepository userRepository
    ) {
        this.bucket = validationValue(bucket,"bucket");
        this.seconds = seconds;
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
                validationValue(accessKey, "accessKey"),
                validationValue(secretKey, "secretKey"));
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(credentials);
        Region r = Region.of(validationValue(region, "region"));

        this.s3 = getS3Client(r, provider);
        this.presigner = getPresigner(r, provider);
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Override
    @Retryable(
            retryFor = { SdkException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public UUID put(UUID id, byte[] data) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(data);

        s3.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(id.toString())
                        .contentType("application/octet-stream")
                        .build(),
                RequestBody.fromBytes(data)
        );
        return id;
    }

    @Override
    public InputStream get(UUID id) {
        Objects.requireNonNull(id);

        return s3.getObject(
                GetObjectRequest.builder()
                        .bucket(bucket)
                        .key(id.toString())
                        .build()
        );
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentResponseDto binaryContentResponseDto) {
        Objects.requireNonNull(binaryContentResponseDto);
        UUID id = Objects.requireNonNull(binaryContentResponseDto.id());

        String presignedUrl = generatePresignedUrl(id.toString(), binaryContentResponseDto.contentType());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, presignedUrl)
                .build();
    }

    @Override
    public void delete(UUID id) {
        Objects.requireNonNull(id);

        s3.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(id.toString())
                        .build()
        );
    }

    private static String validationValue(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("value is null or blank: " + name);
        }
        return value.trim();
    }

    private S3Client getS3Client(Region region, StaticCredentialsProvider provider) {
        return S3Client.builder()
                .region(region)
                .credentialsProvider(provider)
                .build();
    }

    private S3Presigner getPresigner(Region region, StaticCredentialsProvider provider) {
        return S3Presigner.builder()
                .region(region)
                .credentialsProvider(provider)
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(seconds))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);
        return presigned.url().toString();
    }

    @PreDestroy
    void close() {
        try { presigner.close(); } catch (Exception ignored) {}
        try { s3.close(); } catch (Exception ignored) {}
    }

    @Recover
    public UUID recover(SdkException e, UUID id, byte[] data) {
        String requestId = MDC.get("requestId");
        String errorMessage = e.getMessage();

        log. error("S3 upload failed. requestId = {}, binaryContentId = {}, errorMessage = {}", requestId, id, errorMessage);

        String title = "S3 파일 업로드 실패";
        String content = """
                작업: BinaryContentStorage.put (S3)
                RequestId: %s
                BinaryContentId: %s
                ErrorMessage: %s
                """.formatted(
                        requestId == null ? "N/A" : requestId,
                id,
                errorMessage == null ? e.getClass().getSimpleName() : errorMessage
        );

        try {
            for (User admin : userRepository.findAllByRole(UserRole.ADMIN)) {
                notificationService.create(admin.getId(), title, content);
            }
        } catch (Exception ex) {
            log.error("Failed to notify admin.");
        }

        throw e;
    }
}
