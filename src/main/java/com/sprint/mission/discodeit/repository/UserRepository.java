package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

public interface UserRepository {
    void createUser(List<User> userList, String message);
    void deleteUser(String name);
//    List<User> getAllUsers();
}
