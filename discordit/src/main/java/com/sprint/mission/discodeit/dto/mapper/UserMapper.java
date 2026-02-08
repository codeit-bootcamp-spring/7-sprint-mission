package com.sprint.mission.discodeit.dto.mapper;

import com.sprint.mission.discodeit.common.config.SessionManager;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final SessionManager sessionManager;

    public UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getRole(),
                user.getUsername(),
                user.getEmail(),
                user.getProfile() == null ? null : BinaryContentMapper.toDto(user.getProfile()),
                sessionManager.isOnline(user.getUsername())
        );
    }
}
