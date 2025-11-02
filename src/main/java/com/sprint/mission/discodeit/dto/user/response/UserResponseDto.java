package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Builder
@ToString
public class UserResponseDto {
    private UUID id;
    private String userName;
    private String nickName;
    private String email;
    private String phoneNum;
    private String loginId;
    private UUID profileId;
    private boolean active;

    public static UserResponseDto from(User user, boolean active) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .loginId(user.getLoginId())
                .profileId(user.getProfileId())
                .active(active)
                .build();
    }
}
