package com.sprint.mission.discodeit.dto.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateUserCommand(
    @NotBlank(message = "이름은 필수입니다.")
    String username, // 유저 이름

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식으로 작성해주세요.")
    String email, // 이메일

    @NotBlank(message = "비밀번호는 필수입니다.")
    String password,

    //프로필파일
    byte[] data, // 데이터 (byte)
    String fileName, //파일 이름
    String fileType //파일 타입
) {

}

