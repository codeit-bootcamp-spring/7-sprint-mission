package com.sprint.mission.discodeit.user.presentation.dto.response;

import java.util.UUID;

public record UserResponseDto(
        String email,
        String username,
        String phoneNumber
) {
}
