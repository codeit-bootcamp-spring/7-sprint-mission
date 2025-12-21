package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints. Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 50, message = "사용자명은 2~50자 사이여야 합니다.")
        String newUsername,

        @Email(message = "올바른 이메일 형식이 아닙니다.")
        @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
        String newEmail,

        @Size(min = 4, max = 60, message = "비밀번호는 4~60자 사이여야 합니다.")
        String newPassword
) {

}
