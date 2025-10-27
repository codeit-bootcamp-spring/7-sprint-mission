package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;

public record ChannelCreateRequestDto(
        UUID serverId,
        String channelName
) {
}
