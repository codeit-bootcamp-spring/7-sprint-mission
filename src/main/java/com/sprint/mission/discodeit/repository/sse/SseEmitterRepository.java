package com.sprint.mission.discodeit.repository.sse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseEmitterRepository {

    private final ConcurrentMap<UUID, List<SseEmitter>> data = new ConcurrentHashMap<>();

    public void save(UUID userId, SseEmitter emitter) {
        data.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    }

    public List<SseEmitter> findById(UUID userId) {
        return data.get(userId);
    }

    public ConcurrentMap<UUID, List<SseEmitter>> findAll() {
        return data;
    }

    public void delete(UUID userId, SseEmitter emitter) {
        List<SseEmitter> emitters = data.get(userId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                data.remove(userId);
            }
        }
    }

    public void deleteAll() {
        data.clear();
    }
}
