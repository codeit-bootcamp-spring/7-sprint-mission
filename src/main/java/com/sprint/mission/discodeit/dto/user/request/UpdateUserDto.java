package com.sprint.mission.discodeit.dto.user.request;

import java.util.UUID;

public record UpdateUserDto(
        String username,
        String password,
        String email,
        String phoneNumber,
        String pronoun,
        UUID profileId
) {
}
