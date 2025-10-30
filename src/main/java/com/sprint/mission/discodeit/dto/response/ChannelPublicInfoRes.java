package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Builder;

@Builder
public record ChannelPublicInfoRes(
        String name,
        String description
)  implements ChannelInfoRes{
    public static ChannelPublicInfoRes from(Channel channel) {
        return ChannelPublicInfoRes.builder()
                .name(channel.getName())
                .description(channel.getDescription())
                .build();
    }
}
