package com.sprint.mission.discodeit.event.listener;

import com.sprint.mission.discodeit.event.UserCacheEvictEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCacheEventListener {
    private final CacheManager cacheManager;

    @EventListener
    public void on(UserCacheEvictEvent event) {
        Cache cache = cacheManager.getCache("users");
        if (cache != null) {
            cache.clear();
            log.debug("유저 캐시 무효화");
        }
    }
}
