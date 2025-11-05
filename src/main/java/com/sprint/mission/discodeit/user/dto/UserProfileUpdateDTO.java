package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.Email;

public record UserProfileUpdateDTO(
        @Email(message = "유효한 이메일 형식이 아닙니다.")
        String email,

        String nickname,

        String phoneNum
) {
}
