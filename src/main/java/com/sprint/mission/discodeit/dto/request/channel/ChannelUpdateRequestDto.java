package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ChannelUpdateRequestDto(
        @Size(min = 1, max = 50)
        String newName,

        @Size(max = 255)
        String newDescription,

        @Min(0)
        Integer slowModeSeconds) {
}
