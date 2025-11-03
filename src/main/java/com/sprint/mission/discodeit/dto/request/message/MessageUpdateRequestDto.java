package com.sprint.mission.discodeit.dto.request.message;

import java.util.List;
import java.util.UUID;

public record MessageUpdateRequestDto(
        String content,
        UUID messageId,
        List<UUID> attachmentIds,
        boolean isDeleted) {
}
