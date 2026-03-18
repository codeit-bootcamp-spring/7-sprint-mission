package com.sprint.mission.discodeit.sse.repository;

import com.sprint.mission.discodeit.sse.model.SseMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class SseMessageRepository {

    private static final int MAX_SIZE = 1000;

    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();

    private final Map<UUID, SseMessage> messages = new ConcurrentHashMap<>();


    public SseMessage save(String eventName, Object data) {

        UUID id = UUID.randomUUID();

        SseMessage message = new SseMessage(id, eventName, data);

        messages.put(id, message);
        eventIdQueue.addLast(id);

        trim();

        return message;
    }


    public List<SseMessage> findAfter(UUID lastEventId) {

        if (lastEventId == null) {
            return List.of();
        }

        List<SseMessage> result = new ArrayList<>();

        boolean found = false;

        for (UUID id : eventIdQueue) {
            if (id.equals(lastEventId)) {
                found = true;
                continue;
            }

            if (found) {
                result.add(messages.get(id));
            }
        }

        if (!found) {
            return List.of();
        }

        return result;
    }


    private void trim() {
        while (eventIdQueue.size() > MAX_SIZE) {

            UUID oldId = eventIdQueue.pollFirst();

            if (oldId != null) {
                messages.remove(oldId);
            }
        }
    }
}
