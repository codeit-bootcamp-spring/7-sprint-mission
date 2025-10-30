package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(UserCreateReq req);
    List<User> findAll();
    User findByEmail(String email);
    List<User> findByNickname(String nickname);
    User delete(UUID id);
    User update(UUID id, String nickname, String password);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
