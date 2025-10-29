package com.sprint.mission.discodeit.dto.request;

public record CreateUserCommand (

    String username, // 유저 이름
    String nickName, // 유저 닉네임
    String email, // 이메일
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
