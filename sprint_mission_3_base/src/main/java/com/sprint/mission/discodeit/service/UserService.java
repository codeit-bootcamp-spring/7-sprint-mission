package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userstatus.UserUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDto create(UserCreateRequest request);
    Optional<UserDto> find(UUID userId);
    List<UserDto> findAll();
    UserDto update(UserUpdateRequest request);
    void delete(UUID userId);
    void updateUsername(UUID userId, String username);
    void updateEmail(UUID userId, String email);
}
