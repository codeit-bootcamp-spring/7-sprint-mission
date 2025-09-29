package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserService {
    void create(String userId,String password,String userName,String userNickname);
    void read(UUID userId);
    void readAll();
    void update(UUID userId);
    void delete(UUID userId);
}
