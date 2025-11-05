package com.sprint.mission.discodeit.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUserNameRequest(
        @NotBlank(message = "변경 할 아이디가 없습니다.")
        @Size(min = 6, max = 20, message = "이름은 2자 이상 20자 이하여야 합니다.")
        String username

) {
}
