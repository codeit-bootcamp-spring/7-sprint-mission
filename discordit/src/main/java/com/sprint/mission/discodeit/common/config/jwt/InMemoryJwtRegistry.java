package com.sprint.mission.discodeit.common.config.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry{
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();

    private final JwtProperties jwtProperties;
    private final JwtProvider jwtProvider;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        UUID userId = jwtInformation.getUserDto().id();
        origin.putIfAbsent(userId, new ArrayDeque<>());
        origin.get(userId).add(jwtInformation);
        if(origin.get(userId).size() > jwtProperties.getMaxActiveJwtAccount()) {
            origin.get(userId).poll();
        }
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.remove(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        return origin.containsKey(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(i -> i.getAccessToken().equals(accessToken));
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(i -> i.getRefreshToken().equals(refreshToken));
    }

    @Override
    public void rotateJwtInformation(String refreshToken, String newRefreshToken) {
        origin.values().stream()
                .flatMap(Queue::stream)
                .filter(i -> i.getRefreshToken().equals(refreshToken))
                .forEach(i -> i.rotate(i.getAccessToken(), newRefreshToken));
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Override
    public void clearExpiredJwtInformation() {
        origin.values().forEach(queue ->
                queue.removeIf(i -> !jwtProvider.validateToken(i.getAccessToken()))
        );
        origin.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
