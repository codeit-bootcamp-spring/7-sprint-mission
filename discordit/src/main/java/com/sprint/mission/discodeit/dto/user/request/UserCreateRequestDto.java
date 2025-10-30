package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequestDto(
        @NotNull(message = "id는 필수입니다.")
        String id,

        @NotNull(message = "비밀번호는 필수입니다.")
        String passwd,

        @Email(message = "올바른 형식의 이메일을 입력해주세요.")
        @NotNull(message = "이메일은 필수입니다.")
        String email,

        @NotNull(message = "닉네임은 필수입니다.")
        String displayName,

        @Nullable
        BinaryContent profileImage) {
}
