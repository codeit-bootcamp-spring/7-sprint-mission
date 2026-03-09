package com.sprint.mission.discodeit.sse.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseEmitterRepository {
    private final ConcurrentMap<UUID, List<SseEmitter>> data = new ConcurrentHashMap<>();

    public void save(UUID userId, SseEmitter emitter) {
        data.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);
    }

    public List<SseEmitter> findByUserId(UUID userId) {
        return data.getOrDefault(userId, List.of());
    }

    public Map<UUID, List<SseEmitter>> findAll() {
        return data;
    }

    public void remove(UUID userId, SseEmitter emitter) {

        List<SseEmitter> emitters = data.get(userId);

        if (emitters == null) return;

        emitters.remove(emitter);

        if (emitters.isEmpty()) {
            data.remove(userId);
        }
    }
}
