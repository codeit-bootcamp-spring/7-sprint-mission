package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record LoginResponseDto (

    UUID userId, //유저 ID
    String username, // 유저 이름
    String nickName, // 유저 닉네임
    String email, // 이메일
    UUID profileId // 프로필 ID
) {
    public static LoginResponseDto from (User user){
        return new LoginResponseDto(
                user.getId(),
                user.getUsername(),
                user.getNickName(),
                user.getEmail(),
                user.getProfileId()
        );
    }
}
