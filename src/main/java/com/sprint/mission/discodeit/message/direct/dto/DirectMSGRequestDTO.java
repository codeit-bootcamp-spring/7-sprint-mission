package com.sprint.mission.discodeit.message.direct.dto;

import java.util.UUID;

public record DirectMSGRequestDTO(
        UUID receiverId,
        UUID senderId,
        String message
) {
}
