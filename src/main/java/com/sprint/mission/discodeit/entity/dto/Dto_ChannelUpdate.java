package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_ChannelUpdate(
        UUID channelID,
        ChannelType channelType,
        String channelName,
        String description
) {
    public static Dto_ChannelUpdate from(UUID channelID, ChannelType channelType, String channelName, String description) {
        return Dto_ChannelUpdate.builder()
                .channelID(channelID)
                .channelType(channelType)
                .channelName(channelName)
                .description(description)
                .build();
    }
}
