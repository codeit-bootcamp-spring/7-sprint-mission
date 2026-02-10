package com.sprint.mission.discodeit.global.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "discodeit.account")
public record AdminAccountProperties(
        String adminEmail,
        String adminName,
        String adminPass
) {
}