package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

  public static UserSimpleInfoRes toSimpleResDto(User user, BinaryContentInfoRes profileImg,
      boolean isOnline) {
    return new UserSimpleInfoRes(
        user.getId(),
        user.getNickname(),
        user.getEmail(),
        profileImg,
        isOnline
    );
  }

  public static UserDetailInfoRes toDetailResDto(User user, BinaryContentInfoRes profileImg,
      boolean isOnline) {
    return new UserDetailInfoRes(
        user.getId(),
        user.getNickname(),
        user.getEmail(),
        profileImg,
        isOnline,
        DateTimeUtil.format(user.getCreatedAt()),
        DateTimeUtil.format(user.getUpdatedAt())
    );
  }
}
