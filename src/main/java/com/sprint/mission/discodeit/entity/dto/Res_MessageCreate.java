package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record Res_MessageCreate(
        UUID channelID,
        UUID userID,
        String message
) {
    public static Res_MessageCreate from(UUID channelID, UUID userID, String message) {
        return Res_MessageCreate.builder()
                .channelID(channelID)
                .channelID(userID)
                .message(message)
                .build();
    }
}
