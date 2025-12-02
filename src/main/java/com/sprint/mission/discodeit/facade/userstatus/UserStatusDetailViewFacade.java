package com.sprint.mission.discodeit.facade.userstatus;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusViewRes;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStateMapper;
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
public class UserStatusDetailViewFacade {

  private final UserStatusService userStatusService;
  private final UserService userService;

  //User id로부터 userStatus 찾기
  public UserStatusViewRes findByUserId(@NonNull UUID userId) {
    userService.findById(userId);               //유저 있는지 확인
    UserStatus userStatus = userStatusService.findByUserId(userId);
    return UserStateMapper.toDetailResDto(userStatus);
  }
}
