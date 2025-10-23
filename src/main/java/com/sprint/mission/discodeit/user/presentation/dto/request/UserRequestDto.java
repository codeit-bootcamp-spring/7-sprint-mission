package com.sprint.mission.discodeit.user.presentation.dto.request;

import java.util.UUID;

public record UserRequestDto(
        UUID id,
        String username
) {
}
