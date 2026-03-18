package com.sprint.mission.discodeit.global.sse;

import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseMessageRepository {
    private static final int MAX_QUEUE_SIZE = 1000;
    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();
    private final Map<UUID, SseMessage> messages = new ConcurrentHashMap<>();

    // 사용자별로 개인 보관함을 제작해서 알림을 순차적으로 쌓아놓겠다.
    private final Map<UUID, List<UUID>> userEventIds = new ConcurrentHashMap<>();

    public UUID save(String eventName, UUID receiverId, Object data) {
        UUID eventId = UUID.randomUUID();
        SseMessage sseMessage = SseMessage.builder()
                .id(eventId)
                .receiverId(receiverId)
                .eventName(eventName)
                .data(data)
                .build();

        messages.put(sseMessage.getId(), sseMessage);
        eventIdQueue.offer(sseMessage.getId());
        // 사용자별에도 저장
        userEventIds.computeIfAbsent(sseMessage.getReceiverId(), k -> new CopyOnWriteArrayList<>())
                .add(sseMessage.getId());

        if (eventIdQueue.size() > MAX_QUEUE_SIZE) {
            UUID poll = eventIdQueue.poll();
            if (poll != null) {
                SseMessage removed = messages.remove(poll);
                if (removed != null) {
                    List<UUID> list = userEventIds.get(removed.getReceiverId());
                    if (list != null) {
                        list.remove(poll);
                    }
                }
            }
        }

        return eventId;
    }

    public List<SseMessage> findMessagesAfter(UUID receiverId, UUID lastId) {
        List<UUID> userIds = userEventIds.getOrDefault(receiverId, Collections.emptyList());
        int index = userIds.indexOf(lastId);

        if (index == -1) {
            return Collections.emptyList();
        } else {
            return userIds.subList(index + 1, userIds.size()).stream()
                    .map(messages::get)
                    .toList();
        }
    }

}
