package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User create(User user);
    List<User> findAll();
    User findByEmail(String email);
    User findByNickname(String nickname);
    User delete(UUID id);
    User update(UUID id, UserUpdateReq req);
}
