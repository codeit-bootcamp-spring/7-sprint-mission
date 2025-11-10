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
    private final UUID id;
    private final String userName;
    private final String nickName;
    private final String email;
    private final String phoneNum;
    private final String loginId;
    private final UUID profileId;
    private final boolean active;

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
