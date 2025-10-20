package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    User insert(User user) throws FileNotFoundException;
    List<User> findAll();
    User findById(UUID id);
    User findByEmail(String email);
    User update(UUID id,
                       String nickname,
                       String password);
    User delete(UUID id);
}
