package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class S3BinaryContentStorage implements BinaryContentStorage {

    private final AwsProperties awsProperties;
    private final BinaryContentRepository binaryContentRepository;

    private S3Client s3Client;
    private S3Presigner s3Presigner;

    private String accesskey;
    private String secretkey;
    private String region;
    private String bucket;
    private int expirationSeconds;

    @PostConstruct
    private void initializeAWSS3(){
        this.accesskey = awsProperties.getS3AccessKey();
        this.secretkey = awsProperties.getS3SecretKey();
        this.region = awsProperties.getS3Region();
        this.bucket = awsProperties.getS3Bucket();
        this.expirationSeconds = awsProperties.getS3PresignedUrlExpiration();

        AwsBasicCredentials credentials = AwsBasicCredentials.create(accesskey,secretkey);
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
    public UUID put(UUID id, byte[] bytes) {

        BinaryContent binaryContent = binaryContentRepository.findById(id).orElseThrow();
        String fileName = binaryContent.getFileName();
        String uniqueName = id+"_"+fileName;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uniqueName)
                .contentType(binaryContent.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(bytes)
        );

        return id;
    }

    @Override
    public InputStream get(UUID fileId) {

        BinaryContent binaryContent = binaryContentRepository.findById(fileId).orElseThrow();
        String fileName = binaryContent.getFileName();
        String uniqueName = fileId+"_"+fileName;

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(uniqueName)
                .build();

        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);
        return response.asInputStream();

    }

    @Override
    public ResponseEntity<Void> download(BinaryContentDto binaryContentDto) {
        String fileName = binaryContentDto.fileName();
        String uniqueName = binaryContentDto.id()+"_"+fileName;

        String presignedUrl = generatePresignedUrl(uniqueName,fileName, binaryContentDto.contentType());
        return ResponseEntity
                .status(HttpStatus.SEE_OTHER)
                .location(URI.create(presignedUrl))
                .build();
    }

    S3Client getS3Client(){
        return this.s3Client;
    }

    String generatePresignedUrl(String key,String fileName, String contentType){

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .responseContentType(contentType)
                .key(key)
                .responseContentDisposition("attachment; filename=\"" + fileName + "\"")
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(expirationSeconds))
                .getObjectRequest(request)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedGetObjectRequest.url().toString();
    }


}
