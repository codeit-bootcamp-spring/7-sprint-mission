package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.Role;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profile,
        Boolean online,
        Role role
) {}
