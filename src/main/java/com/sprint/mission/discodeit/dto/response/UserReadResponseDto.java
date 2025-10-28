package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.util.StaticString;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class UserReadResponseDto {

    private String name;
    private String userName;
    private boolean isOnline;
    private String email;

    public static UserReadResponseDto of(User user, UserStatus userStatus){
        return UserReadResponseDto.builder()
                .name(user.getName())
                .userName(user.getUserName())
                .isOnline(userStatus.isUserOnline())
                .email(user.getEmail())
                .build();
    }
}
