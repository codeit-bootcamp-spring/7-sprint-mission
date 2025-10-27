package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequestDto {
    @NotBlank(message = "유저 이름이 필요합니다.")
    private final String username;// 유저 이름 ( 별명 x)

    @NotBlank(message = "비밀번호가 필요합니다.")
    private final String password;

    @NotBlank(message = "email이 필요합니다.")
    private final String email;
    private final String profileFileName;
    private final String profileContentType;
    private final byte[] profileData;
}
