package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponse getByUserId(String userId);
    UserResponse getById(UUID uuid);
    List<UserResponse> getAllUsers();
    List<UserResponse> getOnlineUsers();

    void signIn(UserCreateRequest dto);
    UserResponse login(String id, String passwd);
    void update(UserUpdateRequest dto);

    void deleteByUserId(String id);
    void deleteById(UUID uuid);
}
