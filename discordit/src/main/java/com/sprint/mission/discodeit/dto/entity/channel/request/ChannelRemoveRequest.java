package com.sprint.mission.discodeit.dto.entity.channel.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelRemoveRequest(
        @NotNull(message = "id는 필수입니다.")
        UUID channelId
) {
}
