package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserDeleteRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getByUserId(String userId);
    UserResponse get(UUID uuid);
    List<UserResponse> getAllUsers();
    List<UserResponse> getOnlineUsers();

    UserResponse signIn(UserCreateRequest dto);
    UserResponse update(UserUpdateRequest dto);

    void deleteByUserId(UserDeleteRequest dto);
    void deleteById(UUID uuid);

    // TODO: Auth로 분리
    UserResponse login(String id, String passwd);
}
