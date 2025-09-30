package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {


    void addUser(User user);


    void removeUser(User user);

    List<User> getAllUser();

    User getUser(UUID id);

    void updateUser(UUID id, UserDto userDto);



}
