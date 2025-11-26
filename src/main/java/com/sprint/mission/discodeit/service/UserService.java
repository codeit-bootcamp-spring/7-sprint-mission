package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserService {
    UserDto create(UserCreateRequest request
            , Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

    UserDto find(UUID userId);

    List<UserDto> findAll();

    UserDto update(UUID userId, UserUpdateRequest userUpdateRequest, Optional<BinaryContentCreateRequest> optionalProfileCreateRequest);

    void delete(UUID userId);
}
