package com.sprint.mission.discodeit.storage.s3;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.UUID;

public class AWSS3Test {

    private S3Client s3Client;

    private S3Presigner s3Presigner;

    @Value("${discodeit.storage.s3.bucket}")
    private String bucketName;
    @Value("${discodeit.storage.s3.access-key}")
    private String accessKey;
    @Value("${discodeit.storage.s3.secret-key}")
    private String secretKey;
    @Value("${discodeit.storage.s3.region}")
    private String region;

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

    public String uploadToS3Bucket(MultipartFile file) throws IOException {
        // 1. 고유한 파일명 생성 (UUID + 원본 파일명)
        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = UUID.randomUUID() + "_" + originalFilename;

        // 2. S3에 업로드할 요청 객체 생성
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName) // 버킷 이름
                .key(uniqueFileName) // 저장될 파일명
                .contentType(file.getContentType())
                .build();

        // 3. 실제 S3에 파일 업로드
        // 전달 방법이 2가지
        s3Client.putObject(
                request,
                // RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                RequestBody.fromBytes(file.getBytes())
        );

        //return generatePresignedUrl(uniqueFileName, 1);
        return s3Client.utilities()
                .getUrl(b -> b.bucket(bucketName).key(uniqueFileName))
                .toString();
    }

    private String generatePresignedUrl(String uniqueFileName, int durationMinutes) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(uniqueFileName)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(durationMinutes))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

    public byte[] downloadFile(String fileUrl) throws Exception {
        String fileName = extractFileNameFromUrl(fileUrl);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> objectAsBytes
                = s3Client.getObjectAsBytes(getObjectRequest);

        return objectAsBytes.asByteArray();
    }

    private static String extractFileNameFromUrl(String imageUrl) throws MalformedURLException, UnsupportedEncodingException {
        URL url = new URL(imageUrl);

        // getPath() -> 프로토콜, ip(도메인), 포트번호를 제외한 리소스 내부 경로만 받음.
        String decodeUrl = URLDecoder.decode(url.getPath(), "UTF-8");
        // 맨 앞에 있는 "/" 떼기 위해서 substring을 진행
        return decodeUrl.substring(1);
    }

}
