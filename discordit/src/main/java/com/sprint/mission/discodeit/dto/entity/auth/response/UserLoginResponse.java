package com.sprint.mission.discodeit.dto.entity.auth.response;

import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public record UserLoginResponse(
        UUID id,
        String username,
        String email,
        BinaryContentDto profile,
        Boolean online
) {
    public static UserLoginResponse toDto(User user) {
        return new UserLoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile() == null? null : BinaryContentMapper.toDto(user.getProfile()),
                user.getOnline()
        );
    }
}
