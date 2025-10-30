package com.sprint.mission.discodeit.dto.update;

public record UpdateUserDto (

    String username, // 유저 이름
    String nickName, // 유저 닉네임
    String email // 이메일
) {}
