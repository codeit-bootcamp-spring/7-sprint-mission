package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

@Builder
public record UserInfoRes(
        String email,
        String nickname,
        byte[] profileImage
) {
    public static UserInfoRes from(User user, byte[] profileImage){
        return UserInfoRes.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .profileImage(profileImage)
                .build();
    }
}
