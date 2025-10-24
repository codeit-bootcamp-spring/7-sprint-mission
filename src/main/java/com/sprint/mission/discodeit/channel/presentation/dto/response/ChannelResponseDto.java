package com.sprint.mission.discodeit.channel.presentation.dto.response;

import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        String channelName,
        UUID serverId,
        List<UUID> history,
        UUID channelId
) {
}
