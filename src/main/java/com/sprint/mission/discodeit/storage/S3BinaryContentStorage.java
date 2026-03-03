package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.UrlResource;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.MDC;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
@Getter
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {


    private S3Client s3Client;
    private S3Presigner s3Presigner;

    private final S3Properties properties;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private int presignedUrlExpiration;

    public S3BinaryContentStorage(
            S3Properties properties,
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.properties = properties;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.accessKey = properties.getAccessKey();
        this.secretKey = properties.getSecretKey();
        this.region = properties.getRegion();
        this.bucket = properties.getBucket();
        this.presignedUrlExpiration = properties.getPresignedUrlExpiration();
    }

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

    @Override
    @Retryable(
            retryFor = RuntimeException.class,
            noRetryFor = IllegalArgumentException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public void put(String fileName, MultipartFile file) {
        String contentType = file.getContentType();
        validateContentType(contentType);
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .contentType(file.getContentType())
                .build();
        try {
            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (IOException e) {
            throw new RuntimeException("S3에 파일 저장 중 문제 발생", e);
        }
    }

    @Recover
    public void recover(RuntimeException e, String fileName, MultipartFile file) {
        String operationName = "S3BinaryContentStorage.put";
        String requestId = MDC.get("requestId");
        String errorMessage = e.getMessage() == null ? e.toString() : e.getMessage();

        String content = "Operation: " + operationName + "\n"
                + "RequestId: " + (requestId == null ? "N/A" : requestId) + "\n"
                + "BinaryContentId: " + fileName + "\n"
                + "Error: " + errorMessage;

        List<User> admins = userRepository.findAllByRole(Role.ADMIN);
        if (admins.isEmpty()) {
            log.error("S3 업로드 최종 실패(관리자 없음): {}", content, e);
        } else {
            List<Notification> notifications = admins.stream()
                    .map(admin -> new Notification("바이너리 데이터 저장 실패", content, admin))
                    .toList();
            notificationRepository.saveAll(notifications);

            log.error("S3 업로드 최종 실패 알림 생성 완료. adminCount={}, requestId={}, fileName={}",
                    admins.size(), requestId, fileName, e);
        }

        throw new RuntimeException("S3 업로드 재시도 모두 실패: " + fileName, e);
    }

    private void validateContentType(String contentType) {
        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + contentType);
        }
    }

    @Override
    public UrlResource getUrlResource(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest =
                    GetObjectPresignRequest.builder()
                            .getObjectRequest(getObjectRequest)
                            .signatureDuration(Duration.ofMinutes(presignedUrlExpiration)) // 유효시간
                            .build();

            URL presignedUrl = s3Presigner
                    .presignGetObject(presignRequest)
                    .url();

            return new UrlResource(presignedUrl);

        } catch (Exception e) {
            throw new IllegalStateException("S3 UrlResource 생성 실패: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.deleteObject(request);
    }
}
