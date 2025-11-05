package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.User;

public record UserSimpleInfoRes(
        String nickname,            //닉네임
        BinaryContentInfoRes profileImg,        //프로필 이미지
        boolean isOnline            //온라인 상태
) {
    public static UserSimpleInfoRes from(User user, BinaryContentInfoRes profileImg){
        return new UserSimpleInfoRes(
                user.getNickname(),
                profileImg,
                false
        );
    }
}
