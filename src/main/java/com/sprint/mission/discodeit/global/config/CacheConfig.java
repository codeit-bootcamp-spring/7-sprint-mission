package com.sprint.mission.discodeit.global.config;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.cache.CaffeineCacheMetrics;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.*;

@Configuration
@EnableCaching
public class CacheConfig {

    //@Bean
    public CacheManager caffeineCacheManager() {

        CaffeineCacheManager cacheManager =
                new CaffeineCacheManager("channelList", "userList", "notificationList");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(10)) // 저장 후 10분이 지나면 자동으로 캐시 만료
                .recordStats() // 통계 수집 활성화
        );

        return cacheManager;
    }

    @Bean
    public ApplicationRunner bindCacheMetrics(
            CacheManager cacheManager,
            MeterRegistry meterRegistry) {
        return args -> {
            cacheManager.getCacheNames().forEach(name -> { // cacheManage에 등록한 캐시 이름 목록 반복
                Cache cache = cacheManager.getCache(name);
                if (cache != null) {
                    CaffeineCacheMetrics.monitor( // Caffeine의 stats() 값을 metrics에 등록
                            meterRegistry,
                            ((CaffeineCache) cache).getNativeCache(),
                            name
                    );
                }
            });
        };
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(ObjectMapper objectMapper) {
        ObjectMapper redisObjectMapper = objectMapper.copy();
        redisObjectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                DefaultTyping.EVERYTHING,
                As.PROPERTY
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
