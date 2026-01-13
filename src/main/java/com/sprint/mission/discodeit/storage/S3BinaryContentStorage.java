package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;


@Component
@ConditionalOnProperty(
        prefix = "discodeit.storage",
        name = "type",
        havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private S3Client s3Client;

    @Value("${spring.cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Value("${spring.cloud.aws.s3.presigned-url-expiration}")
    private long presignedExpiration;

    @PostConstruct // NOTE: 클래스 기반으로 객체가 생성된 후 1번 자동 실행되도록 설정
    private void initializeAmazonS3Client() {
        // 액세스 키와 시크릿 키를 이용해서 계정 인증 받기
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        // 지역 설정 및 인증 정보를 담은 S3Client 객체를 위의 변수에 세팅
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }


    @Override
    public UUID put(UUID binaryId, byte[] bytes) {
        String key = "binary/" + binaryId;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("application/octet-stream")
                .contentLength((long) bytes.length)
                .build();
        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));
            return binaryId;
        } catch (S3Exception e) {
            System.out.println("e = " + e);
            throw new RuntimeException("S3 putObject failed. bucket =" + bucket + ", key =" + key, e);
        }
    }

    @Override
    public InputStream get(UUID binaryId) {

        String key = "binary/" + binaryId;
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            return s3Client.getObject(getObjectRequest); // NOTE: ResponseInputStream 반환, s3 네트워크 요청 발생, 스트리밍이기 때문에 다 올린건 아님
        } catch (NoSuchKeyException e) {
            throw new RuntimeException("S3 object not found. bucket =" + bucket + ", key =" + key, e);
        } catch (S3Exception e) {
            throw new RuntimeException("S3 getObject failed. bucket =" + bucket + ", key =" + key, e);
        }

    }

    @Override
    public ResponseEntity<?> download(BinaryContentResponseDto binaryContentResponseDto) {
//        InputStream inputStream = get(binaryContentResponseDto.id()); // 서버가 직접 다운로드 하는 방식
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + binaryContentResponseDto.fileName() + "\"")
//                .contentType(MediaType.parseMediaType(binaryContentResponseDto.contentType()))
//                .contentLength(binaryContentResponseDto.size())
//                .body(new InputStreamResource(inputStream));

        String key = "binary/" + binaryContentResponseDto.id();

        String presignedUrl = generatePresignedUrl(key, binaryContentResponseDto.contentType());

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, presignedUrl)
                .build();
    }

    private String generatePresignedUrl(String key, String contentType) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        try (S3Presigner presigner = S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build()) {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(contentType) // 프론트는 Content-Type을 동일하게 보내야함
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(presignedExpiration))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presigned = presigner.presignPutObject(presignRequest);
            return presigned.url().toString();
        }
    }

    private S3Client getS3Client() {
        return s3Client;
    }
}
