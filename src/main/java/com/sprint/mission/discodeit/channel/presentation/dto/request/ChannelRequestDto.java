package com.sprint.mission.discodeit.channel.presentation.dto.request;

import java.util.UUID;

public record ChannelRequestDto(
        UUID serverId,
        String channelName,
        UUID channelId
) {
}
