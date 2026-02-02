package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.service.binarycontent.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "s3")
@Getter
public class S3BinaryContentStorage implements BinaryContentStorage {


    private S3Client s3Client;
    private S3Presigner s3Presigner;

    private final S3Properties properties;
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private int presignedUrlExpiration;

    public S3BinaryContentStorage(S3Properties properties) {
        this.properties = properties;
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
    public UUID put(MultipartFile file) {
        String contentType = file.getContentType();
        validateContentType(contentType);
        String fileName = UUID.randomUUID().toString();
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
            throw new RuntimeException("S3에 파일 저장 중 문제 발생");
        }
        return UUID.fromString(fileName);
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
