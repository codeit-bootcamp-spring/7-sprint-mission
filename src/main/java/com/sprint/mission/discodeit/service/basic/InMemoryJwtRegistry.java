package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.JwtInformation;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.JwtRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InMemoryJwtRegistry implements JwtRegistry {

    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount = 1;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        UUID userId = jwtInformation.getUserDto().id();
        Queue<JwtInformation> jwt = origin.computeIfAbsent(userId, k -> new ConcurrentLinkedQueue<>());
        while (jwt.size() >= maxActiveJwtCount) {
            jwt.poll();
        }
        jwt.add(jwtInformation);

        log.info("JWT 등록: User={}", jwtInformation.getUserDto());
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.remove(userId);
        log.info("JWT 삭제: userId={}", userId);
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        return origin.containsKey(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(jwtInformation -> jwtInformation
                        .getAccessToken().equals(accessToken));
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(jwtInformation -> jwtInformation
                        .getRefreshToken().equals(refreshToken));
    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
        UUID userId = newJwtInformation.getUserDto().id();
        Queue<JwtInformation> jwt = origin.get(userId);
        jwt.removeIf(jwtInformation -> jwtInformation.getRefreshToken().equals(refreshToken));
        jwt.add(newJwtInformation);
        log.info("JWT 로테이션 완료 - userId={}", newJwtInformation.getUserDto());
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @CacheEvict(value = "allUsers", allEntries = true)
    @Override
    public void clearExpiredJwtInformation() {
        origin.values().forEach(queue ->
                queue.removeIf(jwtInformation ->
                        !jwtTokenProvider.isRefreshTokenValid(jwtInformation.getRefreshToken())
                )
        );
        origin.values().removeIf(Queue::isEmpty);
        log.info("만료된 JWT 삭제 완료");
    }

    @Override
    public Set<UUID> getAllActiveUserIds() {
        return origin.entrySet().stream()
                .filter(entry -> !entry.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }
}
