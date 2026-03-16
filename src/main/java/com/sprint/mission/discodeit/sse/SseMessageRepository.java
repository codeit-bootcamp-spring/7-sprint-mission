package com.sprint.mission.discodeit.sse;


import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class SseMessageRepository {

    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();
    private final Map<UUID, SseMessage> messages = new ConcurrentHashMap<>();

    public SseMessage save(String eventName, Object data) {
        UUID eventId = UUID.randomUUID();
        SseMessage message = new SseMessage(eventId, eventName, data, Instant.now());

        messages.put(eventId, message);
        eventIdQueue.addLast(eventId);

        return message;
    }

    public List<SseMessage> findAllAfter(UUID lastEventId) {
        List<SseMessage> result = new ArrayList<>();

        boolean shouldCollect = (lastEventId == null);

        for (UUID eventId : eventIdQueue) {
            if (!shouldCollect) {
                if (eventId.equals(lastEventId)) {
                    shouldCollect = true;
                }
                continue;
            }

            if (lastEventId != null && eventId.equals(lastEventId)) {
                continue;
            }

            SseMessage message = messages.get(eventId);
            if (message != null) {
                result.add(message);
            }
        }

        return result;
    }

    public void delete(UUID eventId) {
        messages.remove(eventId);
        eventIdQueue.remove(eventId);
    }

    public void cleanUpOlderThan(Duration duration) {
        Instant cutoff = Instant.now().minus(duration);

        while (true) {
            UUID firstId = eventIdQueue.peekFirst();
            if (firstId == null) {
                break;
            }

            SseMessage message = messages.get(firstId);
            if (message == null) {
                eventIdQueue.pollFirst();
                continue;
            }

            if (message.createdAt().isBefore(cutoff)) {
                eventIdQueue.pollFirst();
                messages.remove(firstId);
                continue;
            }

            break;
        }
    }
}