package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(CreateUserDto createUserDto);
    User getUser(UUID uuid);
    List<User> getAllUsers();
    void updateUser(UpdateUserDto updateUserDto);
    void deleteUser(UUID uuid);
    boolean isExistsUser(UUID userId);

    void addChannelToUser(User user, Channel channel);
    void removeChannelFromAllUsers(Channel channel);
}
