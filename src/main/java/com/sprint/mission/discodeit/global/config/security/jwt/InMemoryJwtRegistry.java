package com.sprint.mission.discodeit.global.config.security.jwt;

import com.sprint.mission.discodeit.event.UserCacheEvictEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@RequiredArgsConstructor
public class InMemoryJwtRegistry implements JwtRegistry {

    // Queue 자료구조로 JwtInformation을 관리하는 이유
    // 1. 단일 값은 동시 로그인이 불가 -> 후보: List, Set, Queue, Stack 등
    // 2. Queue는 Fifo 구조이기 때문에 queue.poll()로 가장 먼저 발급된 Jwt를 제거할 수 있음
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount = 1;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        Queue<JwtInformation> queue = origin.computeIfAbsent(jwtInformation.getUserDto().id(), key -> new ConcurrentLinkedQueue<>());
        queue.offer(jwtInformation);
        if (queue.size() > maxActiveJwtCount) {
            queue.poll();
        }
        // users 캐시 clear
        eventPublisher.publishEvent(new UserCacheEvictEvent(this));
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.remove(userId);
        eventPublisher.publishEvent(new UserCacheEvictEvent(this));
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        Queue<JwtInformation> queue = origin.get(userId);
        return queue != null && !queue.isEmpty();
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(jwtInformation -> jwtInformation.getAccessToken().equals(accessToken));

    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(jwtInformation -> jwtInformation.getRefreshToken().equals(refreshToken));

    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
        origin.computeIfPresent(newJwtInformation.getUserDto().id(), (key, queue) -> {
            queue.stream()
                    .filter(jwtInformation -> jwtInformation.getRefreshToken().equals(refreshToken))
                    .findFirst()
                    .ifPresent(jwtInformation -> {
                        jwtInformation.rotate(newJwtInformation.getAccessToken(), newJwtInformation.getRefreshToken());
                    });

            return queue;
        });
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Override
    public void clearExpiredJwtInformation() {
        origin.entrySet().removeIf(entry -> {
            Queue<JwtInformation> queue = entry.getValue();
            queue.removeIf(jwtInformation ->
                    jwtProvider.isTokenExpired(
                            jwtInformation.getRefreshToken()
                    )
            );
            return queue.isEmpty();
        });
    }

}
