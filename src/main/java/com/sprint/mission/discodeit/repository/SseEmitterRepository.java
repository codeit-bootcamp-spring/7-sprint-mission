package com.sprint.mission.discodeit.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseEmitterRepository {
    private final ConcurrentMap<UUID, List<SseEmitter>> data = new ConcurrentHashMap<>();

    public void save(UUID userId, SseEmitter sseEmitter) {
        data.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(sseEmitter);
    }

    public void delete(UUID userId, SseEmitter sseEmitter) {
        List<SseEmitter> emitters = data.get(userId);
        if(emitters != null) {
            emitters.remove(sseEmitter);
            if(emitters.isEmpty()) {
                data.remove(userId);
            }
        }
    }

    public List<SseEmitter> findByUserId(UUID userId) {
        return data.getOrDefault(userId, List.of());
    }

    public ConcurrentMap<UUID, List<SseEmitter>> findAll() {
        return data;
    }

    public Set<UUID> findConnectedUserIds() {
        return data.keySet();
    }
}
