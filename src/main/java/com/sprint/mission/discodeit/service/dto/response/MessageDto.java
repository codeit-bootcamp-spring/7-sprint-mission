package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.Message;

import java.time.Instant;
import java.util.List;

public record MessageDto(
        String id,
        Instant createdAt,
        Instant updateAt,
        String content,
        String channelId,
        String authorId,
        List<String> attachmentIds
) {

}
