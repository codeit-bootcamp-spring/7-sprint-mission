package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.response.auth.AuthLoginResponseDto;

public interface AuthService {
    AuthLoginResponseDto login(AuthLoginRequestDto authLoginRequestDto);
}
