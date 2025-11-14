package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.AuthServiceDto;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;
import java.util.Optional;

public interface InterfaceUserRepository extends BaseInterfaceRepository<User> {
//    void save(User user);
//    boolean deleteById(UUID readStatusID);
//    Optional<User> findById(UUID readStatusID);
//    List<User> findAll();
//    boolean existsById(UUID readStatusID);
//    boolean existsByName(String thisName);

    Res_UserLogin isLogin(AuthServiceDto authServiceDto);
    boolean isUsingName(String name);
    boolean isUsingEmail(String eMail);;
    Optional<User> findByName(String name);
}