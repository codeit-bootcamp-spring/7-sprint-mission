package com.sprint.mission.discodeit.user.presentation.dto.request;

import java.util.UUID;

public record UserUpdateDto(
        UUID id,
        String email,
        String username,
        String phoneNumber,
        String password
        //null이면 업데이트 하지 않는 걸로
) {
}
