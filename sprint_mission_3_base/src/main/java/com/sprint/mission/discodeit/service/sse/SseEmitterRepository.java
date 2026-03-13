package com.sprint.mission.discodeit.service.sse;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class SseEmitterRepository {

    private final ConcurrentMap<UUID, List<SseEmitter>> data = new ConcurrentHashMap<>();

    public void add(UUID userId, SseEmitter emitter) {
        data.computeIfAbsent(userId, key -> new CopyOnWriteArrayList<>()).add(emitter);
    }

    public List<SseEmitter> findAll(UUID userId) {
        return data.getOrDefault(userId, List.of());
    }

    public ConcurrentMap<UUID, List<SseEmitter>> findAll() {
        return data;
    }

    public void remove(UUID userId, SseEmitter target) {
        data.computeIfPresent(userId, (key, emitters) -> {
            emitters.remove(target);
            return emitters.isEmpty() ? null : emitters;
        });
    }
}