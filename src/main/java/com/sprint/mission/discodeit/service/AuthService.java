package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.response.auth.JwtDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthService {
    public JwtDto refresh(String refreshToken, HttpServletResponse response);
}
