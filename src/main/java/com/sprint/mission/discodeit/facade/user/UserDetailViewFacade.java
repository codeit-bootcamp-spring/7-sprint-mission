package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserDetailViewFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  //유저 단일 조회 : 유저 목록에서 유저를 클릭했을 때
  public UserDetailInfoRes findById(@NonNull UUID userId) {
    User user = userService.findById(userId);
    return toDetailInfo(user);
  }

  //유저 단일 조회 : 닉네임
  public UserDetailInfoRes findByNickname(@NonNull String nickname) {
    User user = userService.findByNickname(nickname);
    return toDetailInfo(user);
  }

  //유저 단일 조회 : 이메일
  public UserDetailInfoRes findByEmail(@NonNull String email) {
    User user = userService.findByEmail(email);
    return toDetailInfo(user);
  }

  //변환 메소드
  private UserDetailInfoRes toDetailInfo(User user) {
    BinaryContentInfoRes profileImg;
    profileImg = BinaryContentInfoRes.from(user.getProfile());
    UserStatus userStatus = userStatusService.findByUserId(user.getId());
    return UserDetailInfoRes.from(user, profileImg, userStatus.isOnline());
  }
}
