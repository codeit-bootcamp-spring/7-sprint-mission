package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

@Builder
public record Dto_ChannelUpdate(
        String channelName, ChannelType channelType, String description
) {
    public static Dto_ChannelUpdate from(String channelName, ChannelType channelType, String description) {
        return Dto_ChannelUpdate.builder()
                .channelName(channelName)
                .channelType(channelType)
                .description(description)
                .build();
    }
}
