package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

@Builder
public record Dto_ChannelUpdate( //all private final
     @NotBlank(message = "channelId is mandatory")
    UUID channelID,
    String channelName,
    String description
) {
    public static Dto_ChannelUpdate from(UUID channelID, ChannelType channelType, String channelName, String description) {
        return Dto_ChannelUpdate.builder()
                .channelID(channelID)
                .channelName(channelName)
                .description(description)
                .build();
    }
}