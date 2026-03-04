package com.sprint.mission.discodeit.common.security.jwt;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryJwtRegistry implements JwtRegistry {
    private final Map<UUID, Queue<JwtInformation>> origin = new ConcurrentHashMap<>();
    private final Map<String, UUID> accessIndex = new ConcurrentHashMap<>();
    private final Map<String, UUID> refreshIndex = new ConcurrentHashMap<>();
    private final Map<UUID, ReentrantLock> userLocks = new ConcurrentHashMap<>();
    private final int maxActiveJwtCount;

    public InMemoryJwtRegistry(int maxActiveJwtCount) {
        if (maxActiveJwtCount < 1) {
            throw new IllegalArgumentException("maxActiveJwtCount must be >= 1");
        }
        this.maxActiveJwtCount = maxActiveJwtCount;
    }

    private ReentrantLock lockOf(UUID userId) {
        return userLocks.computeIfAbsent(userId, k -> new ReentrantLock());
    }

    @Override
    public void registerJwtInformation(JwtInformation info) {
        if (info ==null || info.userDto() == null || info.userDto().id() == null) {
            return;
        }

        UUID userId = info.userDto().id();
        ReentrantLock lock = lockOf(userId);
        lock.lock();

        try {
            Queue<JwtInformation> queue = origin.computeIfAbsent(userId, k -> new ArrayDeque<>());
            while (queue.size() >= maxActiveJwtCount) {
                JwtInformation removed = queue.poll();
                if (removed != null) {
                    removeIndexes(removed);
                }
            }

            queue.offer(info);
            putIndexes(info);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {
        if (userId == null) {
            return;
        }

        Queue<JwtInformation> queue = origin.remove(userId);
        if (queue == null) {
            return;
        }

        for (JwtInformation info : queue) {
            removeIndexes(info);
        }
    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        if (userId == null) {
            return false;
        }

        Queue<JwtInformation> queue = origin.get(userId);
        return queue != null && !queue.isEmpty();
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        if (accessToken == null || accessToken.isBlank()) {
            return false;
        }
        return accessIndex.containsKey(accessToken);
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return false;
        }
        return refreshIndex.containsKey(refreshToken);
    }

    @Override
    public boolean rotateJwtInformation(String refreshToken, JwtInformation newInfo) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return false;
        }
        if (newInfo == null || newInfo.userDto() == null || newInfo.userDto().id() == null) {
            return false;
        }

        UUID userId = refreshIndex.get(refreshToken);
        if (userId == null) {
            return false;
        }

        ReentrantLock lock = lockOf(userId);
        lock.lock();

        try {
            Queue<JwtInformation> queue = origin.get(userId);
            if (queue == null || queue.isEmpty()) {
                return false;
            }

            queue.removeIf(old -> {
                boolean match = refreshToken.equals(old.refreshToken());
                if (match) {
                    removeIndexes(old);
                }
                return match;
            });

            while (queue.size() >= maxActiveJwtCount) {
                JwtInformation removed = queue.poll();
                if (removed != null) {
                    removeIndexes(removed);
                }
            }
            queue.offer(newInfo);
            putIndexes(newInfo);
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clearExpiredJwtInformation() {
        Instant now = Instant.now();

        for (UUID userId : origin.keySet()) {
            ReentrantLock lock = lockOf(userId);
            lock.lock();

            try {
                Queue<JwtInformation> queue = origin.get(userId);
                if (queue == null) {
                    continue;
                }

                queue.removeIf(info -> {
                    boolean expired = info.refreshExpiresAt() != null && !info.refreshExpiresAt().isAfter(now);
                    if (expired) {
                        removeIndexes(info);
                    }
                    return expired;
                });

                if (queue.isEmpty()) {
                    origin.remove(userId, queue);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    private void putIndexes(JwtInformation info) {
        if (info.accessToken() != null && !info.accessToken().isBlank()) {
            accessIndex.put(info.accessToken(), info.userDto().id());
        }
        if (info.refreshToken() != null && !info.refreshToken().isBlank()) {
            refreshIndex.put(info.refreshToken(), info.userDto().id());
        }
    }

    private void removeIndexes(JwtInformation info) {
        if (info.accessToken() != null && !info.accessToken().isBlank()) {
            accessIndex.remove(info.accessToken());
        }
        if (info.refreshToken() != null && !info.refreshToken().isBlank()) {
            refreshIndex.remove(info.refreshToken());
        }
    }
}
