package com.sprint.mission.discodeit.facade.user;


import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.factory.UserFactory;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCreationFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;
  private final UserStatusService userStatusService;
  private final UserFactory userFactory;

  //유저 추가
  public UserDetailInfoRes createUser(@NonNull UserCreateReq req) {
    UUID profileId = null;

    if (req.profileImage().data() != null) {

      BinaryContent profile = binaryContentService.create(
          BinaryContentFactory.create(req.profileImage())
      );
      profileId = profile.getId();
      binaryContentStorage.put(profileId, req.profileImage().data());
    }
    User user = userService.create(userFactory.create(req, profileId));
    userStatusService.create(UserStatus.create(user));
    return UserMapper.toDetailResDto(
        user,
        user.getProfile() == null ?
            null : BinaryContentMapper.toResDto(user.getProfile()),
        true
    );
  }
}
