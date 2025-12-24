package com.sprint.mission.discodeit.dto.entity.user;

import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;

import java.util.UUID;

public record UserDto (
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online
) { }
