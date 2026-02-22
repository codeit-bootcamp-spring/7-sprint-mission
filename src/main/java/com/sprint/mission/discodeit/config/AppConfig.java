package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.service.basic.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
//@EnableJpaAuditing
@EnableScheduling
@RequiredArgsConstructor
public class AppConfig {
    private final AuthService authService;

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void clearExpiredJwtInfo() {
        authService.clearExpiredJwtInfo();
    }
}
