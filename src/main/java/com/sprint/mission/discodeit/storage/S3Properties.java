package com.sprint.mission.discodeit.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "discodeit.storage.s3")
public class S3Properties {
//    @Value("${AWS_S3_ACCESS_KEY}")
//    @Value("${discodeit.storage.s3.access-key}")
    private String accessKey;

//    @Value("${AWS_S3_SECRET_KEY}")
    private String secretKey;

//    @Value("${AWS_S3_REGION}")
    private String region;

//    @Value("${AWS_S3_BUCKET}")
    private String bucket;

//    @Value("${AWS_S3_PRESIGNED_URL_EXPIRATION}")
    private int presignedUrlExpiration;
}
