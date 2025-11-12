package com.sprint.mission.discodeit.service.dto.request;

import java.util.UUID;

public record MessageForm (
        String content,
        UUID authorId,
        UUID channelId
){}
