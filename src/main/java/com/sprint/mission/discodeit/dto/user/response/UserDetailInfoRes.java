package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.DateTimeUtil;

public record UserDetailInfoRes(
        String nickname,        //닉네임
        String email,           //이메일
        BinaryContentInfoRes profileImg,    //프로필 이미지
        boolean isOnline,       //온라인 상태    
        String createAt,         //가입일
        String updateAt         //수정일
) {
    public static UserDetailInfoRes from(User user, BinaryContentInfoRes profileImg, boolean isOnline){
        return new UserDetailInfoRes(
                user.getNickname(),
                user.getEmail(),
                profileImg,
                isOnline,
                DateTimeUtil.format(user.getCreatedAt()),
                DateTimeUtil.format(user.getUpdatedAt())
        );
    }
}
