package com.sprint.mission.discodeit.common.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRegistryCleaner {
    private final JwtRegistry jwtRegistry;

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void clearExpiredJwtInformation() {
        jwtRegistry.clearExpiredJwtInformation();
        log.debug("JwtRegistry cleanup executed");
    }
}
