package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.mapper.dto.JwtInformation;
import com.sprint.mission.discodeit.mapper.dto.UserDto;

////🚨✅로그인 처리는 SecurityFilterChain에서 모두 처리

public interface InterfaceAuthService  {
//    UserDto login(LoginRequest authService);
    UserDto userRoleUpdateRequest(UserRoleUpdateRequest userRoleUpdateRequest);

    JwtInformation refreshToken(String refreshToken);

    void clearExpiredJwtInfo();
}
