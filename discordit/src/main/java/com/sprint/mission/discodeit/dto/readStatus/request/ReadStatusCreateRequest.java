package com.sprint.mission.discodeit.dto.readStatus.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotNull(message = "유저 id는 필수입니다.")
        String userId,
        @NotNull(message = "채널 id는 필수입니다.")
        UUID ChannelId
) {
}
