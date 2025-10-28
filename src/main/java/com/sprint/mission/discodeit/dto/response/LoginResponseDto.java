package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class LoginResponseDto {

    private String name;
    private String userName;
    private String email;

    public static LoginResponseDto of(User user){
        return LoginResponseDto.builder()
                .name(user.getName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .build();
    }
}
