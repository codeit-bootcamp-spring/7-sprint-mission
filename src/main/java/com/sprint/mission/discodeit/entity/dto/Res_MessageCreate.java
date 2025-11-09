package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Res_MessageCreate( //all private final
    @NotBlank(message = "channelID is mandatory")
    UUID channelID,
    @NotBlank(message = "userID is mandatory")
    UUID userID,
    @NotBlank(message = "message is mandatory")
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
