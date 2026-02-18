package com.sprint.mission.discodeit.dto.response.user;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profile,
        Boolean online
) {
}

