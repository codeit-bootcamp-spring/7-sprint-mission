package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;

public record UserCreateReq(String email,
                            String nickname,
                            String password,
                            BinaryContentCreateReq profileImage) {
}
