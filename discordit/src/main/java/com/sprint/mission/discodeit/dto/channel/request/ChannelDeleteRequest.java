package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelDeleteRequest(
        @NotNull(message = "id는 필수입니다.")
        UUID id
) {
}
