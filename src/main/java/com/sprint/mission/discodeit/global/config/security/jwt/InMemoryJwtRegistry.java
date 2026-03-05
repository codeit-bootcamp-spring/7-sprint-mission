package com.sprint.mission.discodeit.global.config.security.jwt;

import com.sprint.mission.discodeit.event.UserCacheEvictEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
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
    private final Set<String> accessTokenIndexes = ConcurrentHashMap.newKeySet();
    private final Set<String> refreshTokenIndexes = ConcurrentHashMap.newKeySet();

    private final int maxActiveJwtCount = 1;
    private final JwtProvider jwtProvider;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        origin.compute(jwtInformation.getUserDto().id(), (key, queue) -> {
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
            }
            queue.offer(jwtInformation);

            while (queue.size() > maxActiveJwtCount) {
                JwtInformation deprecatedJwtInformation = queue.poll();
                if (deprecatedJwtInformation != null) {
                    removeTokenIndex(
                            deprecatedJwtInformation.getAccessToken(),
                            deprecatedJwtInformation.getRefreshToken()
                    );
                }
            }

            addTokenIndex(
                    jwtInformation.getAccessToken(),
                    jwtInformation.getRefreshToken()
            );

            // users 캐시 clear
            eventPublisher.publishEvent(new UserCacheEvictEvent());

            return queue;
        });
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.computeIfPresent(userId, (key, queue) -> {
            queue.forEach(jwtInformation -> {
                removeTokenIndex(
                        jwtInformation.getAccessToken(),
                        jwtInformation.getRefreshToken()
                );
            });
            queue.clear(); // Clear the queue for this user
            return null; // Remove the user from the registry
        });

        eventPublisher.publishEvent(new UserCacheEvictEvent());
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        return origin.containsKey(userId);
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return accessTokenIndexes.contains(accessToken);
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return refreshTokenIndexes.contains(refreshToken);
    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
        origin.computeIfPresent(newJwtInformation.getUserDto().id(), (key, queue) -> {
            queue.stream()
                    .filter(jwtInformation -> jwtInformation.getRefreshToken().equals(refreshToken))
                    .findFirst()
                    .ifPresent(jwtInformation -> {
                        removeTokenIndex(jwtInformation.getAccessToken(), jwtInformation.getRefreshToken());
                        jwtInformation.rotate(newJwtInformation.getAccessToken(), newJwtInformation.getRefreshToken());
                        addTokenIndex(
                                newJwtInformation.getAccessToken(),
                                newJwtInformation.getRefreshToken()
                        );

                    });

            return queue;
        });
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Override
    public void clearExpiredJwtInformation() {
        origin.entrySet().removeIf(entry -> {
            Queue<JwtInformation> queue = entry.getValue();
            queue.removeIf(jwtInformation -> {
                boolean isExpired =
                        !jwtProvider.validateAccessToken(jwtInformation.getAccessToken()) ||
                                !jwtProvider.validateRefreshToken(jwtInformation.getRefreshToken());
                if (isExpired) {
                    removeTokenIndex(
                            jwtInformation.getAccessToken(),
                            jwtInformation.getRefreshToken()
                    );
                }
                return isExpired;
            });
            return queue.isEmpty(); // Remove the entry if the queue is empty
        });
    }

    private void addTokenIndex(String accessToken, String refreshToken) {
        accessTokenIndexes.add(accessToken);
        refreshTokenIndexes.add(refreshToken);
    }

    private void removeTokenIndex(String accessToken, String refreshToken) {
        accessTokenIndexes.remove(accessToken);
        refreshTokenIndexes.remove(refreshToken);
    }

}
