package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.User;

import java.util.List;


public interface UserService {
    void create(String userId,String password,String userName,String userNickname);
    void read(User user);
    void readAll(List<User> users);
    void update(User user);
    void delete(User user);
}
