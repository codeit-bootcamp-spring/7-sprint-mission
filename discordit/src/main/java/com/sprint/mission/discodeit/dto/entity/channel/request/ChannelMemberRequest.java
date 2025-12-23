package com.sprint.mission.discodeit.dto.entity.channel.request;


import java.util.List;
import java.util.UUID;

public record ChannelMemberRequest(
        UUID channelId,
        List<UUID> userUuids
) {
}
