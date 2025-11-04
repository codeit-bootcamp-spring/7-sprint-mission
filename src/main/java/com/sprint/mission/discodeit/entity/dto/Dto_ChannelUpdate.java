package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

@Builder
public record Dto_ChannelUpdate(
        ChannelType channelType,
        String channelName,
        String description
) {
    public static Dto_ChannelUpdate from(ChannelType channelType, String channelName, String description) {
        return Dto_ChannelUpdate.builder()
                .channelType(channelType)
                .channelName(channelName)
                .description(description)
                .build();
    }
}
