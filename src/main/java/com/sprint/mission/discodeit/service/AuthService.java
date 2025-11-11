package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.auth.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

public interface AuthService {

    /**
     * 로그인
     * @return 로그인 성공 시 User, 실패 시 예외 발생
     */
    User login(LoginRequestDto request);
    boolean checkLoginInfo(String loginId, String password);
}
