package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequest(
        @NotNull(message = "id는 필수입니다.")
        String userId,

        @Nullable String passwd,
        @Nullable String displayName,
        @Email @Nullable String email,
        @Nullable String bio,
        @Nullable OnlineStatus onlineStatus,
        @Nullable BinaryContent profileImage
) {
}
