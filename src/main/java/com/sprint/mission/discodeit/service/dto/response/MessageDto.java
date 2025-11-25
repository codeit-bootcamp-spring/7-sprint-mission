package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updateAt,
        String content,
        UUID channelId,
        UUID authorId,
        List<String> attachmentIds
) {

}
