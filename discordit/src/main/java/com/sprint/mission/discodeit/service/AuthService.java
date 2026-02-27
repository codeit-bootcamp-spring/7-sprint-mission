package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.auth.response.JwtDto;

public interface AuthService {
    JwtDto refresh(String refreshToken);
}
