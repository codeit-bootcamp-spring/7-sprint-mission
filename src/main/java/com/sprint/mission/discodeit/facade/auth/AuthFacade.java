package com.sprint.mission.discodeit.facade.auth;

import com.sprint.mission.discodeit.dto.auth.request.UserLoginReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthFacade {

  private final UserService userService;
  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;

  //로그인
  public UserDetailInfoRes login(@NonNull UserLoginReq req) {
    User user = userService.findByNickname(req.nickname());
    if (user == null) {
      throw new CustomException(ErrorCode.INVALID_NICKNAME);
    }
    if (!user.getPassword().equals(req.password())) {
      throw new CustomException(ErrorCode.INVALID_PASSWORD);
    }
    UserStatus userStatus = userStatusService.findByUserId(user.getId());
    userStatusService.updateOnlineAt(userStatus.getId());
    userStatusService.updateOfflineAt(userStatus.getId());

    return UserDetailInfoRes.from(user,
        user.getProfileId() == null ? null
            : binaryContentService.getBinaryContent(user.getProfileId()),
        true);
  }

  //로그아웃
  public void logout(@NonNull UUID userId) {
    userStatusService.findByUserId(userId);
    UserStatus userStatus = userStatusService.findByUserId(userId);
    userStatusService.updateOfflineAt(userStatus.getId());
  }
}
