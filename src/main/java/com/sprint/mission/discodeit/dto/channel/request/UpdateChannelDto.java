package com.sprint.mission.discodeit.dto.channel.request;

import com.sprint.mission.discodeit.entity.enums.ChannelType;

import java.util.UUID;

public record UpdateChannelDto(
        UUID channelId,
        ChannelType channelType,
        String channelName,
        String desc
) {
}
