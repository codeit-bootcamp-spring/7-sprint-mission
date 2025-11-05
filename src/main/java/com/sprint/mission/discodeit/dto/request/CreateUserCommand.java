package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserCommand (

    @NotBlank(message = "이름은 필수입니다.")
    String username, // 유저 이름

    @NotBlank (message = "닉네임은 필수입니다.")
    String nickName, // 유저 닉네임

    @NotBlank (message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식으로 작성해주세요.")
    String email, // 이메일

    @NotBlank (message = "비밀번호는 필수입니다.")
    String password, // 유저 비밀번호

    //프로필파일
    byte[] data, // 데이터 (byte)
    String fileName, //파일 이름
    String fileType //파일 타입
) {
    public static CreateUserCommand from(
            CreateUserRequestDto userDto,
            CreateBinaryContentRequestDto contentDto
    ) {
        if(contentDto == null){
            return new CreateUserCommand(
                    userDto.username(),
                    userDto.nickName(),
                    userDto.email(),
                    userDto.password(),
                    null, null, null
            );
        }

        return new CreateUserCommand(
                userDto.username(),
                userDto.nickName(),
                userDto.email(),
                userDto.password(),
                contentDto.data(),
                contentDto.fileName(),
                contentDto.fileType()
        );
    }
}
