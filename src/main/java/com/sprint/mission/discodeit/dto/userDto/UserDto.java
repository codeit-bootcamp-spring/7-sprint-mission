package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.entity.role.Role;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online,
        Role role
) {
}
