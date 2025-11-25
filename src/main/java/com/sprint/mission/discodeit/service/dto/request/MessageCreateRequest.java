package com.sprint.mission.discodeit.service.dto.request;

public record MessageCreateRequest(
        String content,
        String userId,
        String channelId
){}
