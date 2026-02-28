package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
@EnableScheduling
@EnableRetry
@EnableAsync
@EnableCaching //for. Caffeine 캐시
public class AppConfig {
    private final AuthService authService;

    @Scheduled(fixedDelay = 1_000 * 60 * 5)
    public void clearExpiredJwtInfo() {
        authService.clearExpiredJwtInfo();
    }
}
