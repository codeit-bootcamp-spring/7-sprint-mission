package com.sprint.mission.discodeit.dto.userStatus.request;

import com.sprint.mission.discodeit.enums.OnlineStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserStatusUpdateByUserRequest(
        @NotNull(message = "유저 id는 필수입니다.")
        UUID userUuid,
        @NotNull(message = "상태는 필수입니다.")
        OnlineStatus onlineStatus
) {
}
