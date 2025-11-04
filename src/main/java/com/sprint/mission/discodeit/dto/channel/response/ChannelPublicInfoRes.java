package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ChannelPublicInfoRes(
        String type,
        String name,
        String description,
        Instant lastMessageTime
)  implements ChannelInfoRes{
    public static ChannelPublicInfoRes from(Channel channel, Instant lastMessageTime) {
        return ChannelPublicInfoRes.builder()
                .type(channel.getPublicType().getValue())
                .name(channel.getName())
                .description(channel.getDescription())
                .lastMessageTime(lastMessageTime)
                .build();
    }
}
