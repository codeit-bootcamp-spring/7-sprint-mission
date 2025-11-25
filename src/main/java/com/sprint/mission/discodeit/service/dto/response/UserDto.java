package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

public record UserDto(
        String id,
        String email,
        String username,
        BinaryContentDto profile,
        boolean online
) {

}
