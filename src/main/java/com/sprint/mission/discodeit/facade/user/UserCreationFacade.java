package com.sprint.mission.discodeit.facade.user;


import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.factory.UserFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.transactional.CustomTransactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserCreationFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;
  private final UserFactory userFactory;

  //유저 추가
  @CustomTransactional
  public UserDetailInfoRes createUser(@NonNull UserCreateReq req) {
    UUID profileId = null;

    if (req.profileImage().data() != null) {

      BinaryContent profile = binaryContentService.create(
          BinaryContentFactory.create(req.profileImage())
      );

      profileId = profile.getId();
    }
    User user = userService.create(userFactory.create(req, profileId));
    userStatusService.create(UserStatus.create(user));
    return UserDetailInfoRes.from(
        user,
        user.getProfile() == null ?
            null : BinaryContentInfoRes.from(user.getProfile()),
        true
    );
  }
}
