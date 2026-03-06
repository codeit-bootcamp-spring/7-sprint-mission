package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

//    @Bean // NOTE: Caffeine에서 Redis로 yml 설정하기위해 기존 @Bean 비활성화
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

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {
        ObjectMapper redisObjectMapper = objectMapper.copy();
        redisObjectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.EVERYTHING,
                JsonTypeInfo.As.PROPERTY
        );

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer(redisObjectMapper)
                        )
                )
                .prefixCacheNameWith("discodeit:")
                .entryTtl(Duration.ofSeconds(600))
                .disableCachingNullValues();
    }


}
