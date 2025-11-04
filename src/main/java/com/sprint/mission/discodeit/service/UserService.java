package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserService {
    UserCreateResponse create(UserCreateRequest request);
    UserFindResponse find(UUID userId);
    List<UserFindResponse> findAll();
    UserUpdateResponse update(UUID uuid, UserUpdateRequest userUpdateRequest);
    void delete(UUID userId);
}
