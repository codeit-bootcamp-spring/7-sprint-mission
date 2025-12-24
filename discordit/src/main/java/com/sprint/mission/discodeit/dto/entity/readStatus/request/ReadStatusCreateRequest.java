package com.sprint.mission.discodeit.dto.entity.readStatus.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull(message = "유저 id는 필수입니다.")
        UUID userId,
        @NotNull(message = "채널 id는 필수입니다.")
        UUID channelId
) {
}
