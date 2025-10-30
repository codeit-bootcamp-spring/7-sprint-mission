package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequestDto(
        @NotNull
        List<UUID> userIds,
        Integer slowModeSeconds,

        @NotNull
        ChannelType channelType) {
}
