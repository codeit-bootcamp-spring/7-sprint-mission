package com.sprint.mission.discodeit.dto.response.message;

import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        String content,
        String userName,
        UUID authorId,
        UUID channelId,
        List<UUID> attachmentIds,
        boolean isDeleted) {
}
