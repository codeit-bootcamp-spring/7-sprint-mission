package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.Builder;

@Builder
public record UserDetailInfoRes(
        String nickname,        //닉네임
        String email,           //이메일
        byte[] profileImage,    //프로필 이미지
        boolean isOnline,       //온라인 상태    
        String createAt         //가입일
) {
    public static UserDetailInfoRes from(User user,byte[] profileImage, boolean isOnline){
        return UserDetailInfoRes.builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .profileImage(profileImage)
                .isOnline(isOnline)
                .createAt(DateTimeUtil.format(user.getCreatedAt()))
                .build();
    }
}
