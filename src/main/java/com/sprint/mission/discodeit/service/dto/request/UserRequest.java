package com.sprint.mission.discodeit.service.dto.request;

import java.util.UUID;

public record UserRequest(
        UUID id,
        String username
) {
}
