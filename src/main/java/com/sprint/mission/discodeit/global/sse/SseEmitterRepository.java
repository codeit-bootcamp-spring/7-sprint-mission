package com.sprint.mission.discodeit.global.sse;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseEmitterRepository {
    // 다중 탭 지원을 위한 List 구조
    // 사용자 당 N개의 연결을 허용. (예: 다중 탭)
    private final Map<UUID, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void save(UUID userId, SseEmitter emitter) {
        emitters.computeIfAbsent(userId, k -> new CopyOnWriteArrayList<>()).add(emitter);
    }

    // 특정 사용자의 Emitter List 조회
    public List<SseEmitter> findAllById(UUID userId) {
        return emitters.getOrDefault(userId, Collections.emptyList());
    }

    // 모든 사용자의 users
    public List<UUID> findAllUserIds() {
        return new CopyOnWriteArrayList<>(emitters.keySet());
    }

    // 모든 사용자 조회
    public List<SseEmitter> findAll() {
        return emitters.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    public void deleteByUserId(UUID userId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }

    public void deleteByEmitter(SseEmitter emitter) {
        emitters.forEach((userId, userEmitters) -> {
            if (userEmitters.remove(emitter)) {
                if (userEmitters.isEmpty()) {
                    emitters.remove(userId);
                }
            }
        });
    }
}