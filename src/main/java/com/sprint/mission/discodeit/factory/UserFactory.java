package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.User;

public class UserFactory {
    private UserFactory() {}

    public static User create(UserCreateReq req){
        return User.builder()
                .email(req.email())
                .nickname(req.nickname())
                .password(req.password())
                .profileId(req.profileId())
                .build();
    }
}
