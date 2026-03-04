package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.common.security.jwt.InMemoryJwtRegistry;
import com.sprint.mission.discodeit.common.security.jwt.JwtRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtRegistryConfig {

    @Bean
    public JwtRegistry jwtRegistry() {
        return new InMemoryJwtRegistry(1);
    }
}
