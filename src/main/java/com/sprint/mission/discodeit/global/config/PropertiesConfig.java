package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.global.config.security.AdminAccountProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AdminAccountProperties.class)
public class PropertiesConfig {
}
