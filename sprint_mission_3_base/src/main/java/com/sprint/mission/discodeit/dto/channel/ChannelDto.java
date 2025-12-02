package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String name
) {
    public static ChannelDto from(Channel channel) {
        return new ChannelDto(
                channel.getId(),
                channel.getName()
        );
    }
}
