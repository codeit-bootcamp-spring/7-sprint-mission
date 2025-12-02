package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusViewRes;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStateMapper {

  public static UserStatusSimpleViewRes toSimpleResDto(UserStatus userStatus) {
    return new UserStatusSimpleViewRes(userStatus.isOnline());
  }

  public static UserStatusViewRes toDetailResDto(UserStatus userStatus) {
    return new UserStatusViewRes(
        userStatus.getUser().getId(),
        userStatus.isOnline(),
        DateTimeUtil.format(userStatus.getOfflineAt())
    );
  }
}
