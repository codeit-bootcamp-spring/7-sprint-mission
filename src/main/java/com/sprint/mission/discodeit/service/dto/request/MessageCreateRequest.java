package com.sprint.mission.discodeit.service.dto.request;

import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID authorId,
        UUID channelId
){}
