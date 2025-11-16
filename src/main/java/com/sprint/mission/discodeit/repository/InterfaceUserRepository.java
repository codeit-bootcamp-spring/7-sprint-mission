package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.AuthServiceDto;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import java.util.Optional;

public interface InterfaceUserRepository extends BaseInterfaceRepository<User> {
    Res_UserLogin isLogin(AuthServiceDto authServiceDto);
    boolean isUsingName(String name);
    boolean isUsingEmail(String eMail);;
    Optional<User> findByName(String name);
}