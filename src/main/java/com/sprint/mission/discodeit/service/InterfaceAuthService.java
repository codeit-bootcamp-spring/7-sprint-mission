package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_AuthService;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;

public interface InterfaceAuthService extends BaseInterfaceService {
    Res_UserLogin isLogin(Dto_AuthService authService);
}
