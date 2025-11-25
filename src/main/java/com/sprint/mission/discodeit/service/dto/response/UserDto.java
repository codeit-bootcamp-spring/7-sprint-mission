package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

import java.util.UUID;

public record UserDto(
        UUID id,
        String email,
        String username,
        BinaryContentDto profile,
        boolean online
) {

}
