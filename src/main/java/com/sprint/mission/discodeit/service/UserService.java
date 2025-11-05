package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(CreateUserDto createUserDto);

    UserResponseDto getUser(UUID uuid);

    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUser(UUID userId, UpdateUserDto updateUserDto);

    void deleteUser(UUID uuid);
}
