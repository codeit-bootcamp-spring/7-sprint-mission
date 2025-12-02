package com.sprint.mission.discodeit.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

  @Bean
  @ConditionalOnProperty(value = "discodeit.storage.type", havingValue = "local")
  public Path localStoragePath(@Value("${discodeit.storage.local.root-path}") String rootPath) {
    return Path.of(rootPath);
  }
}
