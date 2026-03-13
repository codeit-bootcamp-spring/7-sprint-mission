package com.sprint.mission.discodeit.repository;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class SseMessageRepository {

    private static final int MAX_MESSAGES = 1000;

    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();
    private final Map<UUID, SseMessage> messages = new ConcurrentHashMap<>();

    public UUID save(UUID receiverId, String eventName, Object data) {
        UUID eventId = UUID.randomUUID();

        SseMessage message = new SseMessage(
                eventId,
                receiverId,
                eventName,
                data,
                Instant.now()
        );

        messages.put(eventId, message);
        eventIdQueue.addLast(eventId);

        trimIfNecessary();

        return eventId;
    }

    public List<SseMessage> findMessagesAfter(UUID receiverId, UUID lastEventId) {
        if (lastEventId == null) {
            return List.of();
        }

        List<SseMessage> result = new ArrayList<>();
        boolean found = false;

        for (UUID eventId : eventIdQueue) {
            if (!found) {
                if (eventId.equals(lastEventId)) {
                    found = true;
                }
                continue;
            }

            SseMessage message = messages.get(eventId);
            if (message == null) {
                continue;
            }

            if (message.getReceiverId().equals(receiverId)) {
                result.add(message);
            }
        }

        return result;
    }

    public Optional<SseMessage> findById(UUID eventId) {
        return Optional.ofNullable(messages.get(eventId));
    }

    public void delete(UUID eventId) {
        messages.remove(eventId);
        eventIdQueue.remove(eventId);
    }

    private void trimIfNecessary() {
        while (eventIdQueue.size() > MAX_MESSAGES) {
            UUID oldestId = eventIdQueue.pollFirst();
            if (oldestId != null) {
                messages.remove(oldestId);
            }
        }
    }
}
