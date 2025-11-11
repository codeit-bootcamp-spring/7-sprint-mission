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
    private final String realName;
    private final String nickName;
    private final String email;
    private final String phoneNum;
    private final String username;
    private final UUID profileId;
    private final boolean online;

    public static UserResponseDto from(User user, boolean online) {
        return UserResponseDto.builder()
                .id(user.getId())
                .realName(user.getRealName())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .phoneNum(user.getPhoneNum())
                .username(user.getUsername())
                .profileId(user.getProfileId())
                .online(online)
                .build();
    }
}
