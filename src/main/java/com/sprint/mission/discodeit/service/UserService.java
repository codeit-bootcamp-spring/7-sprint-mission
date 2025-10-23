package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.req.UserCreatedReq;
import com.sprint.mission.discodeit.entity.User;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

public interface UserService {
    User create(UserCreatedReq req);
    User findById(UUID id);
    List<User> findAll();
    User findByEmail(String email);
    User findByNickname(String nickname);
}
