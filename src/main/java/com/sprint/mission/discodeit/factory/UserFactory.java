package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;

public class UserFactory {

  private UserFactory() {
  }

  public static User create(UserCreateReq req, BinaryContent profile) {
    return User.createWithProfile(
        req.email(),
        req.nickname(),
        req.password(),
        profile
    );
  }
}
