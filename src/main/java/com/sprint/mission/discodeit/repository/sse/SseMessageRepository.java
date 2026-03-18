package com.sprint.mission.discodeit.repository.sse;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class SseMessageRepository {

    private static final int MAX_EVENT_SIZE = 1000;

    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();
    private final Map<UUID, SseMessage> messages = new ConcurrentHashMap<>();

    public void save(UUID eventId, SseMessage message) {
        if (eventIdQueue.size() >= MAX_EVENT_SIZE) {
            UUID oldestEventId = eventIdQueue.poll();
            if (oldestEventId != null)
                messages.remove(oldestEventId);
        }
        eventIdQueue.add(eventId);
        messages.put(eventId, message);
    }

    public List<SseMessage> findEventSince(UUID lastEventId) {
        List<SseMessage> events = new ArrayList<>();
        boolean found = false;

        for (UUID eventId : eventIdQueue) {
            if (found) {
                events.add(messages.get(eventId));
            } else if (eventId.equals(lastEventId)) {
                found = true;
            }
        }
        return events;
    }
}
