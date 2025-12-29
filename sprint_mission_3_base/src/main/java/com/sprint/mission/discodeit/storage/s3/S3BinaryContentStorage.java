package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final S3Client s3;
    private final S3Presigner presigner;
    private final S3StorageProperties props;

    public S3BinaryContentStorage(S3Client s3, S3Presigner presigner, S3StorageProperties props) {
        this.s3 = s3;
        this.presigner = presigner;
        this.props = props;
    }

    @Override
    public UUID put(UUID binaryContentId, byte[] bytes) {
        String key = binaryContentId.toString();

        if (exists(key)) {
            throw new IllegalArgumentException("File with key " + binaryContentId + " already exists");
        }

        PutObjectRequest req = PutObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .build();

        s3.putObject(req, RequestBody.fromBytes(bytes));
        return binaryContentId;
    }

    @Override
    public InputStream get(UUID binaryContentId) {
        String key = binaryContentId.toString();

        try {
            ResponseInputStream<?> stream = s3.getObject(
                    GetObjectRequest.builder()
                            .bucket(props.bucket())
                            .key(key)
                            .build()
            );
            return stream;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                throw new NoSuchElementException("File with key " + binaryContentId + " does not exist");
            }
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> download(BinaryContentDto metaData) {
        String key = metaData.id().toString();

        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(props.bucket())
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + metaData.fileName() + "\"")
                .responseContentType(metaData.contentType())
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofSeconds(props.presignedUrlExpiration()))
                        .getObjectRequest(getReq)
                        .build()
        );

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, presigned.url().toString())
                .build();
    }

    private boolean exists(String key) {
        try {
            s3.headObject(HeadObjectRequest.builder()
                    .bucket(props.bucket())
                    .key(key)
                    .build());
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw e;
        }
    }
}
