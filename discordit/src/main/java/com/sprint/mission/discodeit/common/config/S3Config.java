package com.sprint.mission.discodeit.common.config;

import com.sprint.mission.discodeit.common.config.properties.AwsS3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@ConditionalOnProperty(
        name = "discodeit.storage.type",
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3Config {

    private final AwsS3Properties properties;

    @Bean
    public S3Client s3Client() {

        return S3Client.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(properties.region()))
                .credentialsProvider(credentialsProvider())
                .build();
    }

    private StaticCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(properties.accessKey(), properties.SecretKey()));
    }
}
