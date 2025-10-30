package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserCreateReq(String email,
                            String nickname,
                            String password,
                            UUID profileId) {

    public User to(){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .profileId(profileId)
                .build();
    }
}
