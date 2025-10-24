package com.sprint.mission.discodeit.dto.user.response;

import java.util.UUID;

public record UserCreateResponse(
        UUID userId,
        String username,
        String userNickname
) {
}
