package com.sprint.mission.discodeit.dto.readStatus.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadStatusUpdateRequest(
        @NotNull(message = "유저 아이디는 필수입니다.")
        String userId,
        @NotNull(message = "채널 아이디는 필수입니다.")
        UUID channelId,
        @NotNull(message = "읽은 시간은 필수입니다.")
        LocalDateTime readTime
) {
}
