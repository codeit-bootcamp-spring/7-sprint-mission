package com.sprint.mission.discodeit.application.dto.request;

import java.util.List;
import java.util.UUID;

public record ChannelCreateRequestDto(
        UUID serverId,
        String channelName,
        List<UUID> membersId,
        boolean isPrivate
) {
}
