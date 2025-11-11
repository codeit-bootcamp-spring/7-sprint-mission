package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface AuthService {
    UserResponseDto login(AuthLoginRequestDto authLoginRequestDto);
    void logout(UUID userId);
}
