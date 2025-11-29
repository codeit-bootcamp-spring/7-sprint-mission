package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
public class UserDeletionFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  //유저 삭제
  @CustomTransactional
  public void deleteUser(@NonNull UUID userId) {
    User user = userService.findById(userId);
    UserStatus userStatus = userStatusService.findByUserId(userId);

    if (user.getProfile() != null) {
      binaryContentService.delete(user.getProfile().getId());
    }
    if (userStatus != null) {
      userStatusService.delete(userStatus.getId());
    }
    userService.delete(userId);


  }
}
