package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  UserResponseDto createUser(CreateUserDto createUserDto,
      Optional<CreateBinaryContentDto> createBinaryContentDto);

  UserResponseDto getUser(UUID uuid);

  List<UserResponseDto> getAllUsers();

  UserResponseDto updateUser(UUID userId, UpdateUserDto updateUserDto,
      Optional<CreateBinaryContentDto> createBinaryContentDto);

  void deleteUser(UUID uuid);
}
