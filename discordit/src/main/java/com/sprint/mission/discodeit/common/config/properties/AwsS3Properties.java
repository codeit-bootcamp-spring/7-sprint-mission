package com.sprint.mission.discodeit.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "aws.s3")
public record AwsS3Properties(
    String accessKey,
    String SecretKey,
    String region,
    String bucket,
    Long presignedUrlExpiration
) {
}
