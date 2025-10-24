package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserService {
    UserCreateResponse create(UserCreateRequest request);
    User find(UUID userId);
    List<User> findAll();
    User update(UUID userId, String newUsername, String newEmail, String newPassword, String UserNickName,String newProfilePicture);
    void delete(UUID userId);
}
