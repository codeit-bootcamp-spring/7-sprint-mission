package com.sprint.mission.discodeit.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry {

    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();

    private final int maxActiveJwtCount = 1;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        UUID userId = jwtInformation.getUserDto().getId();


        Queue<JwtInformation> userQueue = origin.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());

        userQueue.add(jwtInformation);

        while (userQueue.size() > maxActiveJwtCount) {
            userQueue.poll();
        }
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.remove(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        Queue<JwtInformation> queue = origin.get(userId);
        return queue != null && !queue.isEmpty();
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return origin.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(info -> info.getAccessToken().equals(accessToken));
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return origin.values().stream()
                .flatMap(Collection::stream)
                .anyMatch(info -> info.getRefreshToken().equals(refreshToken));
    }

    @Override
    public void rotateJwtInformation(String oldRefreshToken, JwtInformation newJwtInformation) {
        Queue<JwtInformation> userQueue = origin.get(newJwtInformation.getUserDto().getId());

        if (userQueue != null) {
            userQueue.removeIf(info -> info.getRefreshToken().equals(oldRefreshToken));
            userQueue.add(newJwtInformation);
        }
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Override
    public void clearExpiredJwtInformation() {
        Instant now = Instant.now();

        origin.entrySet().removeIf(entry -> {
            Queue<JwtInformation> queue = entry.getValue();

            queue.removeIf(info -> info.getRefreshTokenExpiresAt().isBefore(now));

            return queue.isEmpty();
        });
    }
    @Override
    public void invalidateJwtInformationByRefreshToken(String refreshToken) {
        origin.entrySet().removeIf(entry -> {
            Queue<JwtInformation> queue = entry.getValue();

            queue.removeIf(info -> info.getRefreshToken().equals(refreshToken));

            return queue.isEmpty();
        });
    }
}
