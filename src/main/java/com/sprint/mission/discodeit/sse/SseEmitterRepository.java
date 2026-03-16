package com.sprint.mission.discodeit.sse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class SseEmitterRepository {

    private final ConcurrentMap<UUID, List<SseEmitter>> data = new ConcurrentHashMap<>();

    public void save(UUID receiverId, SseEmitter emitter) {
        data.compute(receiverId, (key, emitters) -> {
            List<SseEmitter> newEmitters = (emitters == null) ? new ArrayList<>() : new ArrayList<>(emitters);
            newEmitters.add(emitter);
            return newEmitters;
        });
    }

    public List<SseEmitter> findAllByReceiverId(UUID receiverId) {
        List<SseEmitter> emitters = data.get(receiverId);
        return emitters == null ? List.of() : List.copyOf(emitters);
    }

    public void delete(UUID receiverId, SseEmitter emitter) {
        data.computeIfPresent(receiverId, (key, emitters) -> {
            List<SseEmitter> newEmitters = new ArrayList<>(emitters);
            newEmitters.remove(emitter);
            return newEmitters.isEmpty() ? null : newEmitters;
        });
    }

    public void deleteAll(UUID receiverId) {
        data.remove(receiverId);
    }

    public ConcurrentMap<UUID, List<SseEmitter>> findAll() {
        return data;
    }
}