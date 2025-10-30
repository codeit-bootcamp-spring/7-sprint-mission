package com.sprint.mission.discodeit.dto.response.auth;

import java.util.UUID;

public record AuthLoginResponseDto(
        UUID userId,
        String username) {
}
