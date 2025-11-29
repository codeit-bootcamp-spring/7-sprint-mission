package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

  private final BinaryContentService binaryContentService;

  public User create(UserCreateReq req, UUID profileId) {
    BinaryContent profile = binaryContentService.findById(profileId);
    return User.createWithProfile(
        req.email(),
        req.nickname(),
        req.password(),
        profile
    );
  }
}
