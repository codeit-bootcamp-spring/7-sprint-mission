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
@Slf4j
@EnableCaching
public class CasheConfig {

    @Bean
    public CacheManager caffeineCacheManager(){
        log.info("카페인 캐시 매니저  초기화");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100) // 캐시 최대 1000 개 , 넘으면 LRU 방식으로 제거
                .expireAfterWrite(Duration.ofMinutes(10)) // 저장후 10분이 지나면 자동으로 만료
                .recordStats() //통계 수집 활성화
        );

        cacheManager.registerCustomCache("notifications",
                Caffeine.newBuilder()
                        .maximumSize(500)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .recordStats()
                        .build()
        );
        cacheManager.registerCustomCache("users",
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .recordStats()
                        .build()
        );

        cacheManager.registerCustomCache("channels",
                Caffeine.newBuilder()
                        .maximumSize(100)
                        .expireAfterWrite(Duration.ofMinutes(10))
                        .recordStats()
                        .build()
        );
        return cacheManager;
    }
}
