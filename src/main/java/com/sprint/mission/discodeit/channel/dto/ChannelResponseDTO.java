package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponseDTO(
        UUID id,
        String channelName,
        ChannelType channelType,
        String topic,
        boolean isPrivate,
        Instant createDate
) {
    public static ChannelResponseDTO from(Channel channel) {
        return new ChannelResponseDTO(
                channel.getId(),
                channel.getChannelName(),
                channel.getChannelType(),
                channel.getTopic(),
                channel.isPrivate(),
                channel.getCreatedAt()
        );
    }
}
