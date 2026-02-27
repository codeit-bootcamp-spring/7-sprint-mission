package com.sprint.mission.discodeit.common.config.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jwt.secret")
public class JwtProperties {
    private String key;
    private Long accessTokenExpiration;
    private Long refreshTokenExpiration;
    private String issuer;
    private int maxActiveJwtAccount;
}
