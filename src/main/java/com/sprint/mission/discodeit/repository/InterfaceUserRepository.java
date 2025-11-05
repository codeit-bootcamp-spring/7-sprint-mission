package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;

public interface InterfaceUserRepository extends BaseInterfaceRepository<User> {
//    void save(User user);
//    boolean deleteById(UUID readStatusID);
//    Optional<User> findById(UUID readStatusID);
//    Optional<List<User>> findAll();
//    boolean existsById(UUID readStatusID);
//    boolean existsByName(String thisName);

    Res_UserLogin isLogin(String name, String password);
    boolean isUsingName(String name);
    boolean isUsingEmail(String eMail);;
}