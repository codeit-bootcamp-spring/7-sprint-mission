package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.request.LoginUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;

public interface AuthService {

    /**
     * 로그인
     * @return 로그인 성공 시 User, 실패 시 예외 발생
     */
    UserResponseDto login(LoginUserDto request);
}
