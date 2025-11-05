package com.sprint.mission.discodeit.dto.user.request;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import jakarta.validation.constraints.Pattern;

public record UserCreateReq(
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
        String email,
        @Pattern(regexp = "^[\\w가-힣]{2,}$")
        String nickname,
        @Pattern(regexp = "^.{6,}$")
        String password,
        BinaryContentCreateReq profileImage) {
    public UserCreateReq {
        if (profileImage == null) {
            profileImage = new BinaryContentCreateReq(null, null, null);
        }
    }
}
