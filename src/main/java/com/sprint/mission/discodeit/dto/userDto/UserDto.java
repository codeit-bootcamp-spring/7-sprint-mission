package com.sprint.mission.discodeit.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online
) {

    public static UserDto from(User user, boolean isOnline) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile() != null ? BinaryContentDto.from(user.getProfile()) : null,
                isOnline
        );
    }
}
