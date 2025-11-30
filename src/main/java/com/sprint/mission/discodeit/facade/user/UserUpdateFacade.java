package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BinaryContentFactory;
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
public class UserUpdateFacade {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;
  private final BinaryContentStorage binaryContentStorage;

  //유저 수정
  public UserDetailInfoRes updateUser(@NonNull UUID userId, @NonNull UserUpdateReq req) {
    User user = userService.findById(userId);

    //이메일과 닉네임 부터 update(조건 맞지 않으면 바로 예외처리)
    userService.update(userId, req);

    //기존 프로필 사진 있으면 무조건 삭제
    if (user.getProfile() != null) {
      binaryContentService.delete(user.getProfile().getId());
      binaryContentStorage.delete(user.getProfile().getId());
      user.updateProfile(null);
    }

    //올라온 데이터가 있으면 무조건 만들어서 배정
    if (req.profileImage() != null) {
      BinaryContent profileImg = binaryContentService.create(
          BinaryContentFactory.create(req.profileImage())
      );
      user.updateProfile(profileImg);
      binaryContentStorage.put(profileImg.getId(), req.profileImage().data());
    }
    userStatusService.updateByUserId(userId);

    return UserMapper.toDetailResDto(
        user,
        user.getProfile() == null ?
            null : BinaryContentMapper.toResDto(user.getProfile()),
        true
    );
  }
}
