package com.sprint.mission.discodeit.application.dto.response;

import java.util.UUID;

public record ChannelResponseDto(
        String channelName,
        UUID serverId,
        UUID channelId
) {
}
