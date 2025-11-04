package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.Builder;

@Builder
public record UserDetailInfoRes(
        String nickname,        //닉네임
        String email,           //이메일
        BinaryContentInfoRes profileImg,    //프로필 이미지
        boolean isOnline,       //온라인 상태    
        String createAt         //가입일
) {
    public static UserDetailInfoRes from(User user, BinaryContentInfoRes profileImg, boolean isOnline){
        return UserDetailInfoRes.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImg(profileImg)
                .isOnline(isOnline)
                .createAt(DateTimeUtil.format(user.getCreatedAt()))
                .build();
    }
}
