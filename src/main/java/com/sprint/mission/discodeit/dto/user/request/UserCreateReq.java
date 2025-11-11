package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;

public record UserCreateReq(
        String email,
        String nickname,
        String password,
        BinaryContentCreateReq profileImage)
{
    public UserCreateReq {
        if (profileImage == null) {
            profileImage = new BinaryContentCreateReq(null, null, null);
        }
    }

    public static UserCreateReq from(UserInfoReq infoReq, BinaryContentCreateReq profileImage) {
        return new UserCreateReq(infoReq.email(), infoReq.nickname(), infoReq.password(), profileImage);
    }
}
