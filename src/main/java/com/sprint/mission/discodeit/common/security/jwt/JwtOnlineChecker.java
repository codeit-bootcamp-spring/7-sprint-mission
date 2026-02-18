package com.sprint.mission.discodeit.common.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtOnlineChecker {
    private final JwtRegistry jwtRegistry;

    public boolean isOnline(UUID userId) {
        return userId != null && jwtRegistry.hasActiveJwtInformationByUserId(userId);
    }

    public Map<UUID, Boolean> onlineMap(Set<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<UUID, Boolean> result = new HashMap<>();
        for (UUID userId : userIds) {
            result.put(userId, jwtRegistry.hasActiveJwtInformationByUserId(userId));
        }

        return result;
    }
}
