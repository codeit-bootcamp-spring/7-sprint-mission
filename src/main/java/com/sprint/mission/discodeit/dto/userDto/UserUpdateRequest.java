package com.sprint.mission.discodeit.dto.userDto;

import jakarta.validation.constraints.Pattern;

public record UserUpdateRequest(
        String newUsername,
        
        @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 올바르지 않습니다.")
        String newEmail,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$", message = "비밀번호 형식이 올바르지 않습니다.")
        String newPassword
) {
}
