package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotBlank;

public record PublicChannelCreateRequestDto(
        @NotBlank
        String name,
        String description,
        Integer slowModeSeconds,
        ChannelType type) {
}
