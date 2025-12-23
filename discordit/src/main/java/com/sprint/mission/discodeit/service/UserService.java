package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.user.UserDto;
import com.sprint.mission.discodeit.dto.entity.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.request.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto get(UUID uuid);

    List<UserDto> getAllUsers();

    UserDto signIn(UserCreateRequest dto, MultipartFile profile);

    UserDto update(UUID id, UserUpdateRequest dto, MultipartFile profile);

    void delete(UUID id);
}
