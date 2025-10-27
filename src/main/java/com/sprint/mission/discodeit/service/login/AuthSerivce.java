package com.sprint.mission.discodeit.service.login;

import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;

public interface AuthSerivce {
    UserInfoDto login(LoginRequestDto loginRequestDto);
}
