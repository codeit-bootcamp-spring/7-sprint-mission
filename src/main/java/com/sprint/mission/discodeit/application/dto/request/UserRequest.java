package com.sprint.mission.discodeit.application.dto.request;

import java.util.UUID;

public record UserRequest(
        UUID id,
        String username
) {
}
