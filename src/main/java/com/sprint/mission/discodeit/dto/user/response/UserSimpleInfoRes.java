package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

@Builder
public record UserSimpleInfoRes(
        String nickname,            //닉네임
        BinaryContentInfoRes profileImg,        //프로필 이미지
        boolean isOnline            //온라인 상태
) {
    public static UserSimpleInfoRes from(User user, BinaryContentInfoRes profileImg, boolean isOnline){
        return UserSimpleInfoRes.builder()
                .nickname(user.getNickname())
                .profileImg(profileImg)
                .isOnline(isOnline)
                .build();
    }
}
