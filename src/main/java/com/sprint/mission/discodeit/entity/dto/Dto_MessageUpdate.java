package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_MessageUpdate(
        UUID messageID, String message
) {
    public static Dto_MessageUpdate from(UUID messageID, String message) {
        return Dto_MessageUpdate.builder()
                .messageID(messageID)
                .message(message)
                .build();
    }
}
