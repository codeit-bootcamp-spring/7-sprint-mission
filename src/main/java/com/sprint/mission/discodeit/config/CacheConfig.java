package com.sprint.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(
                Caffeine.newBuilder()
                        .maximumSize(1000)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .recordStats()

        );

        cacheManager.registerCustomCache("channelsByUserId",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofSeconds(10))
                        .recordStats()
                        .build()
        );

        cacheManager.registerCustomCache("notificationsByUserId",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofSeconds(10))
                        .recordStats()
                        .build()
                );

        cacheManager.registerCustomCache("users",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofSeconds(10))
                        .recordStats()
                        .build()
                );

        return cacheManager;
    }


}
