package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserUpdateFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  //유저 수정
  public UserDetailInfoRes updateUser(@NonNull UUID userId, @NonNull UserUpdateReq req) {
    User user = userService.findById(userId);
    UUID profileId = null;

    //이메일과 닉네임 부터 update(조건 맞지 않으면 바로 예외처리)
    userService.update(userId, req);

    //기존 프로필 사진 있으면 무조건 삭제
    if (user.getProfile() != null) {
      binaryContentService.delete(user.getProfile().getId());
      userService.updateProfileImage(userId, null);
    }
    //올라온 데이터가 있으면 무조건 만들어서 배정
    if (req.profileImage() != null) {
      BinaryContent profileImg = binaryContentService.create(
          BinaryContentFactory.create(req.profileImage())
      );
      profileId = profileImg.getId();
    }
    System.out.println(profileId);
    userService.updateProfileImage(userId, profileId);
    userStatusService.updateByUserId(userId);

    return UserDetailInfoRes.from(
        user,
        user.getProfile() == null ?
            null : BinaryContentInfoRes.from(user.getProfile()),
        true
    );
  }
}
