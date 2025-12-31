package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${AWS_S3_BUCKET}")
    private String bucket;

    @Value("${AWS_S3_PRESIGNED_URL_EXPIRATION:600}")
    private long presignedUrlExpiration;

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String fileName = binaryContentId.toString();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

        return binaryContentId;
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
                .location(URI.create(presignedUrl))
                .build();

    }

    private String generatePresignedUrl(String key, String contentType) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .responseContentType(contentType)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(presignedUrlExpiration))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

}
