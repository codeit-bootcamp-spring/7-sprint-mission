package com.sprint.mission.discodeit.dto.user.request;

import java.util.UUID;

public record UserUpdateRequest(
        UUID userId,
        String username,
        String email,
        String Password,
        String userNickname,
        UUID profileImageUrl

) {
}
