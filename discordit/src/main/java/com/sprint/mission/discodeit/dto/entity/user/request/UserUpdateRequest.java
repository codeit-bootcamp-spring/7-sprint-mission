package com.sprint.mission.discodeit.dto.entity.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;

@Schema(description = "사용자 정보 수정 요청")
public record UserUpdateRequest(
        @Schema(description = "새 사용자 이름", example = "newUsername", nullable = true)
        @Nullable
        String newUsername,

        @Schema(description = "새 이메일", example = "newemail@example.com", nullable = true)
        @Email
        @Nullable
        String newEmail,

        @Schema(description = "새 비밀번호", example = "newpass123", nullable = true)
        @Nullable
        String newPassword
) {
}
