package com.sprint.mission.discodeit.dto.entity.user;

import com.sprint.mission.discodeit.common.enums.Roles;
import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;

import java.util.UUID;

public record UserDto (
        UUID id,
        Roles role,
        String username,
        String email,
        BinaryContentDto profile,
        boolean online
) { }
