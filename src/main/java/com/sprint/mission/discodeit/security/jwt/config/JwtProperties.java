package com.sprint.mission.discodeit.security.jwt.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
public class JwtProperties {

    private final String secretKey;

    private final Long accessTokenExpirationMs;

    private final Long refreshTokenExpirationMs;

    private final String issuer;
}
