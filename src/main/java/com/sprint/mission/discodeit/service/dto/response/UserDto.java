package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

public record UserDto(
        String id,
        String email,
        String username,
        BinaryContentDto profile,
        boolean online
) {
    public static UserDto from(User user, BinaryContentDto binaryContentDto) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getUsername(),
                binaryContentDto,
                user.isOnline()
                );
    }
}
