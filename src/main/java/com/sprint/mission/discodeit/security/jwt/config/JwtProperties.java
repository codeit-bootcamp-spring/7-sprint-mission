package com.sprint.mission.discodeit.security.jwt.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;

    private Long accessTokenExpirationMs;

    private Long refreshTokenExpirationMs;

    private String issuer;
}
