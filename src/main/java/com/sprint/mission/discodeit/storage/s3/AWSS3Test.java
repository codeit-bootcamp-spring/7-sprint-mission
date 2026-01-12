package com.sprint.mission.discodeit.storage.s3;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "s3")
public class AWSS3Test {

    private S3Client s3Client;
    private S3Presigner s3Presigner;
    private final AwsProperties awsProperties;



    private String accesskey;
    private String secretkey;
    private String region;
    private String bucket;

    @PostConstruct
    private void initializeAWSS3(){
        this.accesskey = awsProperties.getS3AccessKey();
        this.secretkey = awsProperties.getS3SecretKey();
        this.region = awsProperties.getS3Region();
        this.bucket = awsProperties.getS3Bucket();

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

    public String upload(MultipartFile file) throws IOException {

        String originalName = file.getOriginalFilename();
        String uniqueName = UUID.randomUUID()+"_" + originalName;
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(uniqueName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(file.getBytes())
        );

        return s3Client.utilities()
                .getUrl(x-> x.bucket(bucket).key(uniqueName))
                .toString();
    }

    public void delete(String imageUrl) throws MalformedURLException, UnsupportedEncodingException {

        String key = extractNameFromUrl(imageUrl);
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();
        s3Client.deleteObject(request);
    }


    @SneakyThrows
    public void deleteFiles(List<String> imageUrls) {

        List<String> fileNames = imageUrls.stream().map(x-> {
            try {
                return extractNameFromUrl(x);
            } catch (MalformedURLException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        List<ObjectIdentifier> identifiers = fileNames.stream().map(x ->
                ObjectIdentifier.builder()
                        .key(x)
                        .build()
        ).toList();

        Delete deleteBuild = Delete.builder()
                .objects(identifiers)
                .build();

        DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                .bucket(bucket)
                .delete(deleteBuild)
                .build();

        s3Client.deleteObjects(deleteRequest);
    }

    public byte[] download(String fileUrl) throws MalformedURLException, UnsupportedEncodingException {

        String fileName = extractNameFromUrl(fileUrl);
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        ResponseBytes<GetObjectResponse> response = s3Client.getObjectAsBytes(request);
        return response.asByteArray();
    }

    private String extractNameFromUrl(String imageUrl) throws MalformedURLException, UnsupportedEncodingException {
        URL url = new URL(imageUrl);
        String decode = URLDecoder.decode(url.getPath(),"UTF-8");
        return decode.substring(1);
    }

    public String createPresignedUrl(String fileName, Duration duration) {

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(fileName)
                .build();
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(duration)
                .getObjectRequest(request)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedGetObjectRequest.url().toString();

    }

}
