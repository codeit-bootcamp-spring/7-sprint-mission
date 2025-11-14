package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.AuthServiceDto;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;

public interface InterfaceAuthService extends BaseInterfaceService {
    Res_UserLogin login(AuthServiceDto authService);
}
