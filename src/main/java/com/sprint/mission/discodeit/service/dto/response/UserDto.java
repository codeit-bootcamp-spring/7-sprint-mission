package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.User;

public record UserDto(
        String id,
        String email,
        String username,
        BinaryContentDto profile
) {
    public static UserDto from(User user) {
        return new UserDto(user.getId(),
                user.getEmail(),
                user.getUsername(),
                null
                );
    }
}
