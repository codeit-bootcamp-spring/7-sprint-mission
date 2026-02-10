package com.sprint.mission.discodeit.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "remember-me")
public record SecurityProperties(
        String key,
        int tokenValiditySeconds
) {

}
