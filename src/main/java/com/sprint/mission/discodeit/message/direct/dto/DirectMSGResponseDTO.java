package com.sprint.mission.discodeit.message.direct.dto;

import com.sprint.mission.discodeit.message.direct.DirectMessage;

import java.util.UUID;

public record DirectMSGResponseDTO(
        UUID id,
        UUID receiverId,
        UUID senderId,
        String message
) {
    public static DirectMSGResponseDTO from(DirectMessage directMessage) {
        return new DirectMSGResponseDTO(
                directMessage.getId(),
                directMessage.getReceiverId(),
                directMessage.getSenderId(),
                directMessage.getMessage()
        );
    }
}
