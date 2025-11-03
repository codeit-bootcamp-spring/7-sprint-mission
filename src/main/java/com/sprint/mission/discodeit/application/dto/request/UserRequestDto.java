package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;

public record UserRequestDto(
        UUID id,
        String username
) {
}
