package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

@Builder
public record UserSimpleInfoRes(
        String nickname,            //닉네임
        byte[] profileImage,        //프로필 이미지
        boolean isOnline            //온라인 상태
) {
    public static UserSimpleInfoRes from(User user, byte[] profileImage, boolean isOnline){
        return UserSimpleInfoRes.builder()
                .nickname(user.getNickname())
                .profileImage(profileImage)
                .isOnline(isOnline)
                .build();
    }
}
