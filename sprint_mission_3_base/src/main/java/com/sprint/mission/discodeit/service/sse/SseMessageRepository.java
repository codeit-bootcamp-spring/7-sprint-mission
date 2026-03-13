package com.sprint.mission.discodeit.service.sse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Repository;

@Repository
public class SseMessageRepository {

    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();
    private final Map<UUID, SseMessage> messages = new ConcurrentHashMap<>();
    private final int maxSize = 10000;

    public SseMessage save(SseMessage message) {
        messages.put(message.id(), message);
        eventIdQueue.addLast(message.id());

        while (eventIdQueue.size() > maxSize) {
            UUID oldest = eventIdQueue.pollFirst();
            if (oldest != null) {
                messages.remove(oldest);
            }
        }

        return message;
    }

    public List<SseMessage> findMissedMessages(UUID receiverId, UUID lastEventId) {
        if (lastEventId == null) {
            return List.of();
        }

        List<SseMessage> result = new ArrayList<>();
        boolean collect = false;

        for (UUID eventId : eventIdQueue) {
            if (eventId.equals(lastEventId)) {
                collect = true;
                continue;
            }

            if (collect) {
                SseMessage message = messages.get(eventId);
                if (message != null && receiverId.equals(message.receiverId())) {
                    result.add(message);
                }
            }
        }

        return result;
    }
}