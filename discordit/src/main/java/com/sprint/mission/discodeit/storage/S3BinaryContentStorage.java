package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.common.config.properties.AwsS3Properties;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.s3.S3StorageDownloadException;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.s3.S3StorageException;
import com.sprint.mission.discodeit.common.exceptions.binaryContent.s3.S3StorageUploadException;
import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(
        name = "discodeit.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Presigner s3Presigner;
    private final AwsS3Properties s3Properties;
    private final BinaryContentRepository binaryContentRepository;


    private final S3Client s3Client;

    @Override
    public UUID put(UUID uuid, byte[] content) {
        BinaryContent file = binaryContentRepository.findById(uuid)
                .orElseThrow(() -> new BinaryContentNotFoundException(uuid));

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .key(file.getFileName())
                            .bucket(s3Properties.bucket())
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(content)
            );
        } catch (SdkClientException e) {
            log.warn("파일 업로드에 실패함 : {}", file.getFileName());
            throw new S3StorageUploadException(uuid);
        }
        log.info("S3 파일 업로드 완료 : {}", file.getFileName());
        return uuid;
    }

    @Override
    public InputStream get(String fileName) {
        try {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(s3Properties.bucket())
                    .key(fileName)
                    .build()
            );
        } catch (S3StorageException e) {
            log.warn("파일 다운로드에 실패함");
            throw new S3StorageDownloadException(fileName);
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto dto) {
        try {
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, generatePresignedUrl(dto.fileName()))
                    .build();
        } catch (Exception e) {
            throw new S3StorageDownloadException(dto.fileName());
        }
    }

    @Override
    public void delete(String fileName) {
        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .key(fileName)
                            .bucket(s3Properties.bucket())
                            .build()
            );
        } catch (Exception e) {

        }
    }

    private String generatePresignedUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(s3Properties.presignedUrlExpiration()))
                .getObjectRequest(GetObjectRequest.builder()
                        .bucket(s3Properties.bucket())
                        .key(key)
                        .build())
                .build();

        return s3Presigner.presignGetObject(presignRequest).url().toString();
    }
}
