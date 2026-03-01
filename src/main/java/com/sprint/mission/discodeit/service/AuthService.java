package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;

import java.util.Set;
import java.util.UUID;

public interface AuthService {

    boolean isOnline(UUID userId);

    void expireUserSession(String refreshToken);

    Set<UUID> getOnlineUserIds();

    JwtDto rotateRefreshToken(UserDto userDto);
}
