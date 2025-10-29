package com.sprint.mission.discodeit.dto.request;

public record CreateUserRequestDto (

    String username, // 유저 이름
    String nickName, // 유저 닉네임
    String email, // 이메일
    String password // 유저 비밀번호
) {}

