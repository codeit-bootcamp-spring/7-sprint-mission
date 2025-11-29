package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;

public record UserUpdateReq(
    String email,
    String nickname,
    String password,
    BinaryContentCreateReq profileImage
) {

  public static UserUpdateReq from(UserInfoReq infoReq, BinaryContentCreateReq profileImage) {
    return new UserUpdateReq(infoReq.email(), infoReq.nickname(), infoReq.password(), profileImage);
  }
}
