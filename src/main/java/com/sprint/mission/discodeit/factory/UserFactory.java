package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class UserFactory {
    private UserFactory() {}

    public static User create(UserCreateReq req, UUID profileId){
        return User.builder()
                .email(req.email())
                .nickname(req.nickname())
                .password(req.password())
                .profileId(profileId)
                .build();
    }
}
