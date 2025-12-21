package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequestDto(
        @NotNull
        @Size(min = 2)
        List<UUID> participantIds,

        @Min(0)
        Integer slowModeSeconds,
        ChannelType type) {
}
