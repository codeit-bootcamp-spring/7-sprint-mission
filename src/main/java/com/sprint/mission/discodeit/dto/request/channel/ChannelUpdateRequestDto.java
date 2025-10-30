package com.sprint.mission.discodeit.dto.request.channel;

import java.util.UUID;

public record ChannelUpdateRequestDto(
        UUID channelId,
        String channelName,
        String channelDescription,
        Integer slowModeSeconds) {
}
