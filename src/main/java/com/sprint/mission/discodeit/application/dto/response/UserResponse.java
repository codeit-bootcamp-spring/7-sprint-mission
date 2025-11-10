package com.sprint.mission.discodeit.application.dto.response;

import com.sprint.mission.discodeit.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    String email;
    String username;
    String phoneNumber;
    Boolean isOnline;

    public static UserResponse from(User user){
        return UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .isOnline(user.checkOnline())
                .build();
    }

}
