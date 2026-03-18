package com.sprint.mission.discodeit.global.sse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class SseMessage {

    private final UUID id;           // 이벤트 고유 ID
    private final UUID receiverId; // 클라이언트 ID
    private final String eventName;  // "notifications.created" 등
    private final Object data;       // 직렬화할 DTO

    public static SseMessage of(String eventName, UUID receiverId, Object data) {
        return SseMessage.builder()
                .id(UUID.randomUUID())
                .receiverId(receiverId)
                .eventName(eventName)
                .data(data)
                .build();
    }
}