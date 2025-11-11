package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotBlank;

/**
 * @param username 유저 이름 ( 별명 x)
 */
public record UserCreateRequestDto(
        @NotBlank(message = "유저 이름이 필요합니다.")
        String username,

        @NotBlank(message = "비밀번호가 필요합니다.")
        String password,

        @NotBlank(message = "email이 필요합니다.")
        String email) {
}
