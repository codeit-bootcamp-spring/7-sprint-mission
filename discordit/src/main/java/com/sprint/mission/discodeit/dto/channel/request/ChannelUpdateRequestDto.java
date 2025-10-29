package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ChannelUpdateRequestDto(
        @NotNull(message = "id는 필수입니다.")
        UUID id,
        @NotBlank(message = "채널 이름은 비어있을 수 없습니다.")
        String channelName,
        String description
) {
}
