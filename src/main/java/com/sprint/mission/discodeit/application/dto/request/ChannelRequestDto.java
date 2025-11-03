package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;

public record ChannelRequestDto(
        UUID serverId,
        String channelName,
        UUID channelId
) {
}
