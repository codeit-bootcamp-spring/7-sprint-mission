package com.sprint.mission.discodeit.dto.user.request;

import jakarta.validation.constraints.Pattern;

public record UserFindPasswordReq(
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
    String email,
    @Pattern(regexp = "^[\\w가-힣]{2,}$")
    String nickname
) {

}
