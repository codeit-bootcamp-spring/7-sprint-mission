package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

  private final String accessKey;
  private final String secretKey;
  private final String region;
  private final String bucket;


  public S3BinaryContentStorage(
      @Value("${discodeit.storage.s3.access-key}") String accessKey,
      @Value("${discodeit.storage.s3.secret-key}") String secretKey,
      @Value("${discodeit.storage.s3.region}") String region,
      @Value("${discodeit.storage.s3.bucket}") String bucket) {
    this.accessKey = accessKey;
    this.secretKey = secretKey;
    this.region = region;
    this.bucket = bucket;
  }

  @Override
  public UUID put(UUID binaryContentId, byte[] bytes) {
    PutObjectRequest request = PutObjectRequest.builder()
        .bucket(this.bucket)
        .key(binaryContentId.toString())
        .build();

    getS3Client().putObject(request, RequestBody.fromBytes(bytes));

    return binaryContentId;
  }

  @Override
  public InputStream get(UUID binaryContentId) {
    GetObjectRequest getObjectRequest = GetObjectRequest.builder()
        .bucket(this.bucket)
        .key(binaryContentId.toString())
        .build();

    ResponseBytes<GetObjectResponse> responseBytes = getS3Client().getObjectAsBytes(
        getObjectRequest);

    return responseBytes.asInputStream();
  }

  @Override
  public ResponseEntity<Resource> download(BinaryContentResponseDto responseDto) {
    String url = generatePresignedUrl(
        responseDto.id().toString(),
        responseDto.contentType()
    );
    return ResponseEntity
        .status(HttpStatus.OK)
        .location(URI.create(url))
        .build();
  }

  private S3Client getS3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }

  private String generatePresignedUrl(String key, String contentType) {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
    S3Presigner s3Presigner = S3Presigner.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();

    try {
      GetObjectRequest getObjectRequest = GetObjectRequest.builder()
          .bucket(this.bucket)
          .key(key)
          .build();

      GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
          .signatureDuration(Duration.ofMinutes(5))
          .getObjectRequest(getObjectRequest)
          .build();

      PresignedGetObjectRequest presignedGetObjectRequest =
          s3Presigner.presignGetObject(presignRequest);

      return presignedGetObjectRequest.url().toString();
    } finally {
      s3Presigner.close();
    }
  }
}
