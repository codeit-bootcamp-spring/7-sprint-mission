package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.util.UUID;

public record SimpleChannelDTO(
        UUID channelId,
        String channelName
) {
    public static SimpleChannelDTO from(Channel channel) {
        return new SimpleChannelDTO(channel.getId(), channel.getChannelName());
    }
}
