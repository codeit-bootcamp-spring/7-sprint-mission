package com.sprint.mission.discodeit.event.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCacheEventListener {
    private final CacheManager cacheManager;

    @Async("eventExecutor")
    @EventListener(condition = "#event.equals('USER_CACHE_EVICT')")
    public void on(String event) {
        Cache cache = cacheManager.getCache("users");
        if (cache != null) {
            cache.clear();
            log.debug("유저 캐시 무효화");
        }
    }
}
