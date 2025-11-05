package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;

import java.time.Instant;

public record ChannelPublicInfoRes(
        String type,
        String name,
        String description,
        Instant lastMessageTime
)  implements ChannelInfoRes{
    public static ChannelPublicInfoRes from(Channel channel, Instant lastMessageTime) {
        return new ChannelPublicInfoRes(
                channel.getPublicType().getValue(),
                channel.getName(),
                channel.getDescription(),
                lastMessageTime
        );
    }
}
