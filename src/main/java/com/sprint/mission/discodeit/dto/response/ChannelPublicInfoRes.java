package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ChannelPublicInfoRes(
        String type,
        String name,
        String description,
        Instant lastMessageTime
)  implements ChannelInfoRes{
    public static ChannelPublicInfoRes from(Channel channel, Message message) {
        return ChannelPublicInfoRes.builder()
                .type(channel.getPublicType().getValue())
                .name(channel.getName())
                .description(channel.getDescription())
                .lastMessageTime(message.getCreatedAt())
                .build();
    }
}
