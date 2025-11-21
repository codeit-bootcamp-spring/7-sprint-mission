package com.sprint.mission.discodeit.dto.response.user;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        UUID profileId,
        Boolean online
) {
}