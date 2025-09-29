package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    void createUser(User user);
    void readUser(User user);
    void readAllUser();
    void deleteUser(User user);
    <T>void updateUser(User user,User.userElement userElement, T updatedContent);
    void readUpdatedUser();
    void readDeletedUser();

}
