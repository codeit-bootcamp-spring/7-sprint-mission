package com.sprint.mission.discodeit.global.sse;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;
import java.util.UUID;

public interface SseService {
    public SseEmitter connect(UUID receiverId, UUID lastEventId);

    public void send(Collection<UUID> receiverIds, String eventName, Object data);

    public void broadcast(String eventName, Object data);

    public void cleanUp();
}
