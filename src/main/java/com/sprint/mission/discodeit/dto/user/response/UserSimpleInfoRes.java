package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserSimpleInfoRes(
        UUID userId,                //user UUID
        String nickname,            //닉네임
        String email,               //이메일
        BinaryContentInfoRes profileImg,        //프로필 이미지
        boolean isOnline            //온라인 상태
) {
    public static UserSimpleInfoRes from(User user, BinaryContentInfoRes profileImg, boolean isOnline){
        return new UserSimpleInfoRes(
                user.getId(),
                user.getNickname(),
                user.getEmail(),
                profileImg,
                isOnline
        );
    }
}
