package com.sprint.mission.discodeit.event;

import java.util.Collection;
import java.util.UUID;

public record SseEvent(
        String eventName,
        Object data,
        Collection<UUID> receiverId,
        Boolean isBroadCast // 전체 공지인가,
) {
    public static SseEvent send(Collection<UUID> receiverIds, String name, Object data) {
        return new SseEvent(name, data, receiverIds, false);
    }

    public static SseEvent broadcast(String name, Object data) {
        return new SseEvent(name, data, null, true);
    }
}
