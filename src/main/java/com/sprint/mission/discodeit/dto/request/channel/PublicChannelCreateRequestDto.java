package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequestDto(
        @NotBlank
        @Size(min = 2, max = 30)
        String name,

        @Size(max = 255)
        String description,

        @Min(0)
        Integer slowModeSeconds,
        ChannelType type) {
}
