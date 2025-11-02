package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.Res_UserLogin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceUserRepository extends BaseInterfaceRepository<User> {
    void save(User user);
    void deleteById(UUID id);
    boolean isUsingName(String name);
    boolean isUsingEmail(String eMail);;
    Optional<User> findById(UUID id);
    Optional<List<User>> findAll();
    boolean existsById(UUID id);
    boolean existsByName(String thisName);
    Res_UserLogin isLogin(String name, String password);
}