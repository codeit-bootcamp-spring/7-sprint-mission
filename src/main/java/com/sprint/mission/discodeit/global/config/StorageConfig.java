package com.sprint.mission.discodeit.global.config;

import com.sprint.mission.discodeit.storage.LocalBinaryContentStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class StorageConfig {

    @Value("${discodeit.storage.local.root-path}")
    private String root;

    @ConditionalOnProperty(
            name = "discodeit.storage.type",
            havingValue = "local"
    )
    @Bean
    public LocalBinaryContentStorage localBinaryContentStorage() {
        return new LocalBinaryContentStorage(Path.of(root));
    }
}