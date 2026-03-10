package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.sse.model.SseEvent;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Repository
public class SseMessageRepository {

    private final ConcurrentLinkedDeque<UUID> eventIdQueue = new ConcurrentLinkedDeque<>();
    private final Map<UUID, SseEvent> events = new ConcurrentHashMap<>();

    public void pushMissedEvent(){

    }
}
