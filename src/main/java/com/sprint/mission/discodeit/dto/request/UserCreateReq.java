package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

@Builder
public record UserCreateReq(String email,
                            String nickname,
                            String password) {

    public User to(){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
    }
}
