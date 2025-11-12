package com.sprint.mission.discodeit.dto.channel.request;

import java.util.List;
import java.util.UUID;

public record ChannelUpdateRequest(
        UUID channelId,
        String newChannelName,
        String newDescription
) {
}
