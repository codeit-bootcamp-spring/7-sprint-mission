package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.userDto.UserResponseDto;
import com.sprint.mission.discodeit.entity.dto.loginDto.LoginRequestDto;

public interface AuthSerivce {
    UserResponseDto login(LoginRequestDto loginRequestDto);
}
