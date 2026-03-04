package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.event.S3UploadFailedEvent;
import com.sprint.mission.discodeit.global.config.MDCLoggingInterceptor;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
@Slf4j
public class S3BinaryContentStorage implements BinaryContentStorage {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${AWS_S3_BUCKET}")
    private String bucket;

    @Value("${AWS_S3_PRESIGNED_URL_EXPIRATION:600}")
    private long presignedUrlExpiration;

    @Override
    @Retryable(
            retryFor = {SdkClientException.class}, // 이 예외가 터지면 재시도
            maxAttempts = 3, // 최대 3번
            backoff = @Backoff(delay = 1000, multiplier = 2) // 대기시간 설정
    )
    public UUID put(UUID binaryContentId, byte[] bytes) {
        try {
            String fileName = binaryContentId.toString();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

            return binaryContentId;
        } catch (S3Exception e) {
            // 요청/권한/상태 문제
            log.error(
                    "S3 업로드 실패(비재시도) id={}, statusCode={}",
                    binaryContentId, e.statusCode(), e
            );
            throw new IllegalStateException("S3 upload rejected", e);

        } catch (SdkClientException e) {
            // 네트워크/일시 장애
            log.warn(
                    "S3 업로드 네트워크 실패(재시도) id={}", binaryContentId, e
            );
            throw e;
        }
    }

    @Recover
    public UUID putRecover(SdkClientException e, UUID binaryContentId, byte[] bytes) {
        log.warn("S3 upload 최종 실패 - 파일: {}, 에러메세지: {}", binaryContentId, e.getMessage(), e);
        String requestId = MDC.get(MDCLoggingInterceptor.REQUEST_ID_KEY);
        eventPublisher.publishEvent(new S3UploadFailedEvent(binaryContentId, e, requestId));

        throw new RuntimeException(e);
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String fileName = binaryContentId.toString();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public ResponseEntity<Void> download(BinaryContentResponseDto dto) {
        String fileName = dto.id().toString();
        String presignedUrl = generatePresignedUrl(fileName, dto.contentType());

        return ResponseEntity
                .status(HttpStatus.FOUND)  // 302 리다이렉트
                .header(HttpHeaders.LOCATION, presignedUrl)
                .build();

    }

    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .responseContentDisposition("attachment; filename=\"" + key + "\"")
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

}
