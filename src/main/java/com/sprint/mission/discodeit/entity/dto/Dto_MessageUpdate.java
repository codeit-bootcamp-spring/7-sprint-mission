package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_MessageUpdate( //all private final
    @NotBlank(message = "messageID is mandatory")
    UUID messageID,
    String message
) {
    public static Dto_MessageUpdate from(UUID messageID, String message) {
        return Dto_MessageUpdate.builder()
                .messageID(messageID)
                .message(message)
                .build();
    }
}
