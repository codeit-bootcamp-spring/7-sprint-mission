package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(CreateUserDto createUserDto);

    UserResponseDto getUser(UUID uuid);

    List<UserResponseDto> getAllUsers();

    void updateUser(UpdateUserDto updateUserDto);

    void deleteUser(UUID uuid);
}
