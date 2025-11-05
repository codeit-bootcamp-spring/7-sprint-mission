package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.config.enums.ChannelType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChannelRequestDTO(
        String ChannelName,

        ChannelType channelType,

        String topic,

        boolean isPrivate
) {
}
