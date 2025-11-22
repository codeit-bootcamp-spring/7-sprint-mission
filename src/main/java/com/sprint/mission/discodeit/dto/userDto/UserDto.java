package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online
) {
}
