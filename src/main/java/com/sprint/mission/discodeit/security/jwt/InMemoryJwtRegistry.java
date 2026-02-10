package com.sprint.mission.discodeit.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InMemoryJwtRegistry implements JwtRegistry {

    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount = 1;
    private final JwtTokenProvider jwtTokenProvider;

    public InMemoryJwtRegistry(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        UUID userId = jwtInformation.getUserDto().id();

        origin.compute(userId, (id, queue) -> {

            // 첫 로그인인 경우 Queue 생성
            if (queue == null) {
                queue = new ConcurrentLinkedQueue<>();
            }

            // 최대 로그인 수를 1로 제어
            while (queue.size() >= maxActiveJwtCount) {
                queue.poll(); // 기존 JWT 제거
            }

            queue.offer(jwtInformation); // 새 JWT 등록
            return queue; // Map에 저장
        });
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        origin.remove(userId);
    }

    @Override
    public void invalidateJwtInformationByRefreshToken(String refreshToken) {
        origin.values().forEach(queue->{
            queue.removeIf(info -> info.getRefreshToken().equals(refreshToken));
        });
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
                .anyMatch(info -> info.getAccessToken().equals(accessToken));
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return origin.values().stream()
                .flatMap(Queue::stream)
                .anyMatch(info -> info.getRefreshToken().equals(refreshToken));
    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation newJwtInformation) {
        origin.values().forEach(queue -> {
            queue.forEach(info -> {
                if (info.getRefreshToken().equals(refreshToken)) {
                    info.rotate(
                            newJwtInformation.getAccessToken(),
                            newJwtInformation.getRefreshToken()
                    );
                }
            });
        });
    }

    @Scheduled(fixedDelay = 1000 * 60 * 5)
    @Override
    public void clearExpiredJwtInformation() {
        Iterator<Map.Entry<UUID, Queue<JwtInformation>>> iterator = origin.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, Queue<JwtInformation>> entry = iterator.next();
            Queue<JwtInformation> queue = entry.getValue();

            queue.removeIf(info ->
                    !jwtTokenProvider.isTokenValid(info.getRefreshToken())
            );

            if (queue.isEmpty()) {
                iterator.remove();
            }
        }
    }
}
